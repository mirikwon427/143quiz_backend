package garlicbears.quiz.domain.management.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.common.entity.Admin;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.entity.Role;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import garlicbears.quiz.global.jwt.service.RefreshTokenService;
import garlicbears.quiz.global.jwt.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	private final JwtTokenizer jwtTokenizer;
	private final RefreshTokenService refreshTokenService;

	@Autowired
	public AuthService(JwtTokenizer jwtTokenizer, RefreshTokenService refreshTokenService) {
		this.jwtTokenizer = jwtTokenizer;
		this.refreshTokenService = refreshTokenService;
	}

	public ResponseEntity<?> createTokensAndRespond(Object member, HttpServletResponse response) {
		List<String> roles;
		String email;
		Long memberId;

		if (member instanceof User user) {
			roles = user.getRoles().stream()
				.map(Role::getRoleName)
				.toList();
			email = user.getUserEmail();
			memberId = user.getUserId();
		} else if (member instanceof Admin admin) {
			roles = admin.getRoles().stream()
				.map(Role::getRoleName)
				.toList();
			email = admin.getAdminEmail();
			memberId = admin.getAdminId();
		} else {
			throw new IllegalArgumentException("Invalid user type");
		}

		String accessToken = jwtTokenizer.createAccessToken(email, memberId, roles);
		String refreshToken = jwtTokenizer.createRefreshToken(email, memberId, roles);

		refreshTokenService.save(email, refreshToken, JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUNT);

		addRefreshTokenCookie(response, refreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken);

		return ResponseEntity.ok().headers(headers).body(ResponseDto.success());
	}

	/**
	 * 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급.
	 */
	public String requestRefresh(HttpServletRequest request, HttpServletResponse response) {
		// 쿠키에서 리프레시 토큰을 읽기
		String refreshToken = getRefreshTokenFromCookies(request);

		// 전달받은 유저의 아이디로 유저가 존재하는지 확인
		Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
		String email = claims.getSubject();

		// 전달받은 이메일로 리프레시 토큰이 존재하는지 확인하고, 존재하지 않으면 예외를 발생
		String redisRefreshToken = refreshTokenService.findRefreshToken(email)
			.orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

		// 레디스에 저장된 토큰과 클라이언트로부터 받은 토큰이 일치하는지 확인
		if (!redisRefreshToken.equals(refreshToken)) {
			throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
		}

		long userId = Long.valueOf((Integer)claims.get("id"));
		List roles = (List)claims.get("roles");

		// Token을 발급
		String accessToken = jwtTokenizer.createAccessToken(email, userId, roles);
		String newRefreshToken = jwtTokenizer.createRefreshToken(email, userId, roles);

		// 기존 리프레시 토큰 삭제 후 새로운 리프레시 토큰 저장
		refreshTokenService.deleteRefreshToken(email);
		refreshTokenService.save(email, newRefreshToken, JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUNT);

		// 새로운 리프레시 토큰을 쿠키에 저장
		addRefreshTokenCookie(response, newRefreshToken);

		return accessToken;
	}

	/**
	 * 로그아웃
	 * 전달받은 리프레시 토큰 삭제
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = getRefreshTokenFromCookies(request);
		Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
		String email = claims.getSubject();
		// 리프레시 토큰 삭제
		refreshTokenService.deleteRefreshToken(email);
		// 쿠키에서 리프레시 토큰 삭제
		deleteRefreshTokenCookie(response);
	}

	/**
	 * 리프레시 토큰을 쿠키에 저장
	 */
	private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		// SameSite 속성 추가 ( SameSite = None )
		String cookieHeader = String.format("refreshToken=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=None",
			refreshToken, 7 * 24 * 60 * 60);
		response.addHeader("Set-Cookie", cookieHeader);
	}

	/**
	 * 쿠키에서 리프레시 토큰을 읽음
	 */
	private String getRefreshTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refreshToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
	}

	/**
	 * 리프레시 토큰을 쿠키에서 삭제하는 메서드.
	 */
	private void deleteRefreshTokenCookie(HttpServletResponse response) {
		// SameSite 속성 추가 ( SameSite = None )
		String cookieHeader = "refreshToken=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=None";
		response.addHeader("Set-Cookie", cookieHeader);
	}
}
