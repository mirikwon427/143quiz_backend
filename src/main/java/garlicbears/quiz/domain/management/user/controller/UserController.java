package garlicbears.quiz.domain.management.user.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.service.LogService;
import garlicbears.quiz.domain.management.common.dto.LoginDto;
import garlicbears.quiz.domain.management.common.dto.ResponseUserDto;
import garlicbears.quiz.domain.management.common.service.AuthService;
import garlicbears.quiz.domain.management.user.dto.SignUpDto;
import garlicbears.quiz.domain.management.user.dto.UpdateUserDto;
import garlicbears.quiz.domain.management.user.service.UserRatingService;
import garlicbears.quiz.domain.management.user.service.UserService;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import garlicbears.quiz.global.jwt.service.RefreshTokenService;
import garlicbears.quiz.global.jwt.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import garlicbears.quiz.global.handler.ClientIpHandler;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Tag(name = "회원 관리")
public class UserController implements SwaggerUserController {
	private static final Logger logger = Logger.getLogger(UserController.class.getName());
	private final UserService userService;
	private final UserRatingService userRatingService;
	private final JwtTokenizer jwtTokenizer;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	private final LogService logService;
	private final AuthService authService;

	@Autowired
	UserController(UserService userService,
		UserRatingService userRatingService,
		JwtTokenizer jwtTokenizer,
		PasswordEncoder passwordEncoder,
		RefreshTokenService refreshTokenService,
		LogService logService,
		AuthService authService) {
		this.userService = userService;
		this.userRatingService = userRatingService;
		this.jwtTokenizer = jwtTokenizer;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenService = refreshTokenService;
		this.logService = logService;
		this.authService = authService;
	}

	/**
	 * 회원가입 시 이메일 중복 체크
	 */
	@PostMapping("/checkEmail")
	public ResponseEntity<?> checkEmail(@Valid @RequestBody Map<String, String> request) {
		String email = request.get("email");
		if (email == null || email.trim().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
		userService.checkDuplicatedEmail(email);
		return ResponseEntity.ok(ResponseDto.success());
	}

	/**
	 * 회원가입 시 닉네임 중복 체크
	 */
	@PostMapping("/checkNickname")
	public ResponseEntity<?> checkNickname(@Valid @RequestBody Map<String, String> request) {
		String nickname = request.get("nickname");
		if (nickname == null || nickname.trim().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
		userService.checkDuplicatedNickname(nickname);
		return ResponseEntity.ok(ResponseDto.success());
	}

	/**
	 * 회원가입
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto,
		BindingResult bindingResult) {
		// 유효성 검사 에러가 있는 경우
		if (bindingResult.hasErrors()) {
			StringBuilder errorMessage = new StringBuilder();
			bindingResult.getFieldErrors()
				.forEach(error -> errorMessage.append(error.getField())
					.append(": ")
					.append(error.getDefaultMessage())
					.append("."));
			logger.warning("errorMessage : " + errorMessage.toString());
			throw new CustomException(ErrorCode.BAD_REQUEST);
		}
		userService.checkDuplicatedEmail(signUpDto.getEmail());
		userService.checkDuplicatedNickname(signUpDto.getNickname());
		userService.signUp(signUpDto);
		return ResponseEntity.ok(ResponseDto.success());
	}

	@GetMapping("/")
	public ResponseEntity<?> searchUser(@AuthenticationPrincipal UserDetails userDetails,
		HttpServletRequest request) {
		if (userDetails == null) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}
		// 현재 인증된 사용자의 정보를 UserDetails로부터 가져올 수 있습니다.
		User user = userService.findByEmail(userDetails.getUsername());

		logService.log(user, request.getRequestURI(), ClientIpHandler.getClientIp(request));

		return ResponseEntity.ok(ResponseUserDto.fromUser(user));
	}

	@PatchMapping("/")
	public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody UpdateUserDto updateUserDto) {
		if (userDetails == null) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}
		// 현재 인증된 사용자의 정보를 UserDetails로부터 가져올 수 있습니다.
		User user = userService.findByEmail(userDetails.getUsername());

		userService.update(user, updateUserDto);

		return ResponseEntity.ok(ResponseUserDto.fromUser(user));
	}

	@DeleteMapping("/")
	public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}
		// 현재 인증된 사용자의 정보를 UserDetails로부터 가져올 수 있습니다.
		User user = userService.findByEmail(userDetails.getUsername());

		userService.delete(user);

		return ResponseEntity.ok(ResponseDto.success());
	}

	@PostMapping("/rating")
	public ResponseEntity<?> rating(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody Map<String, Double> request) {
		if (userDetails == null) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}
		// 현재 인증된 사용자의 정보를 UserDetails로부터 가져올 수 있습니다.
		User user = userService.findByEmail(userDetails.getUsername());
		Double ratingValue = request.get("ratingValue");

		userRatingService.saveRating(user, ratingValue);

		return ResponseEntity.ok(ResponseDto.success());
	}

	/**
	 * 로그인
	 * 유효한 사용자 정보를 전달받아 액세스 토큰과 리프레시 토큰을 발급.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult,
		HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		User user = userService.findByEmail(loginDto.getEmail());
		if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		return authService.createTokensAndRespond(user, response);
	}

	/**
	 * 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급.
	 */
	@GetMapping("/reissue")
	public ResponseEntity<?> requestRefresh(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = authService.requestRefresh(request, response);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken);

		return ResponseEntity.ok().headers(headers).body(ResponseDto.success());
	}


	/**
	 * 로그아웃
	 * 전달받은 리프레시 토큰 삭제
	 */
	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		authService.logout(request, response);
		return ResponseEntity.ok(ResponseDto.success());
	}

}
