package garlicbears.quiz.domain.management.admin.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.common.entity.Admin;
import garlicbears.quiz.domain.common.entity.Role;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.management.admin.service.AdminService;
import garlicbears.quiz.domain.management.common.dto.LoginDto;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import garlicbears.quiz.global.jwt.dto.RefreshTokenDto;
import garlicbears.quiz.global.jwt.service.RefreshTokenService;
import garlicbears.quiz.global.jwt.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private static final Logger logger = Logger.getLogger(AdminController.class.getName());
	private final AdminService adminService;
	private final JwtTokenizer jwtTokenizer;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;

	public AdminController(AdminService adminService,
		JwtTokenizer jwtTokenizer,
		PasswordEncoder passwordEncoder,
		RefreshTokenService refreshTokenService) {
		this.adminService = adminService;
		this.jwtTokenizer = jwtTokenizer;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenService = refreshTokenService;
	}

	/**
	 * 로그인
	 * 유효한 관리자 정보를 전달받아 액세스 토큰과 리프레시 토큰을 발급.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult,
		HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		Admin admin = adminService.findByEmail(loginDto.getEmail());
		if (!passwordEncoder.matches(loginDto.getPassword(), admin.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		// 사용자의 역할(Role)을 문자열 리스트로 변환
		List<String> roles = admin.getRoles().stream()
			.map(Role::getRoleName)
			.toList();

		// JWT토큰을 생성
		String accessToken = jwtTokenizer.createAccessToken(admin.getAdminEmail(), admin.getAdminId(), roles);
		String refreshToken = jwtTokenizer.createRefreshToken(admin.getAdminEmail(), admin.getAdminId(), roles);

		// 리프레시 토큰을 Redis에 저장
		refreshTokenService.save(admin.getAdminEmail(), refreshToken);

		// 리프레시 토큰을 쿠키에 저장
		response.addCookie(createRefreshTokenCookie(refreshToken));

		HttpHeaders headers = new HttpHeaders();
		headers.set("Access-Token", accessToken);

		return ResponseEntity.ok().headers(headers).body(ResponseDto.success());
	}

	/**
	 * 리프레시 토큰을 쿠키에 저장
	 */
	private Cookie createRefreshTokenCookie(String refreshToken) {
		Cookie refreshTokenCookie = new Cookie("Refresh-Token", refreshToken);
		refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가능하도록 설정
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
		return refreshTokenCookie;
	}

	/**
	 * 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급.
	 */
	@PostMapping("/refreshToken")
	public ResponseEntity<?> requestRefresh(HttpServletRequest request, HttpServletResponse response) {
		// 쿠키에서 리프레시 토큰을 읽기
		String refreshToken = getRefreshTokenFromCookies(request);

		// 전달받은 유저의 아이디로 유저가 존재하는지 확인
		Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
		String email = claims.getSubject();

		// 전달받은 이메일로 리프레시 토큰이 존재하는지 확인하고, 존재하지 않으면 예외를 발생
		refreshTokenService.findRefreshToken(email)
			.orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

		long userId = Long.valueOf((Integer)claims.get("id"));
		List roles = (List)claims.get("roles");

		// Token을 발급
		String accessToken = jwtTokenizer.createAccessToken(email, userId, roles);
		String newRefreshToken = jwtTokenizer.createRefreshToken(email, userId, roles);

		// 기존 리프레시 토큰 삭제 후 새로운 리프레시 토큰 저장
		refreshTokenService.deleteRefreshToken(email);
		refreshTokenService.save(email, newRefreshToken);

		// 새로운 리프레시 토큰을 쿠키에 저장
		response.addCookie(createRefreshTokenCookie(newRefreshToken));

		HttpHeaders headers = new HttpHeaders();
		headers.set("Access-Token", accessToken);

		return ResponseEntity.ok().headers(headers).body(ResponseDto.success());
	}

	/**
	 * 쿠키에서 리프레시 토큰을 읽음
	 */
	private String getRefreshTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("Refresh-Token".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
	}

	/**
	 * 리프레시 토큰을 쿠키에서 삭제하는 메서드.
	 */
	private Cookie deleteRefreshTokenCookie() {
		Cookie refreshTokenCookie = new Cookie("Refresh-Token", null);
		refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가능하도록 설정
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setMaxAge(0); // 쿠키 삭제
		return refreshTokenCookie;
	}

	/**
	 * 로그아웃
	 * 전달받은 리프레시 토큰 삭제
	 */
	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = getRefreshTokenFromCookies(request);
		Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
		String email = claims.getSubject();
		// 리프레시 토큰 삭제
		refreshTokenService.deleteRefreshToken(email);
		// 쿠키에서 리프레시 토큰 삭제
		response.addCookie(deleteRefreshTokenCookie());

		return ResponseEntity.ok(ResponseDto.success());
	}
}
