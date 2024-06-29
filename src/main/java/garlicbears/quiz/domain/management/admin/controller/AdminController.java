package garlicbears.quiz.domain.management.admin.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.common.entity.Admin;
import garlicbears.quiz.domain.common.entity.Role;
import garlicbears.quiz.domain.management.admin.service.AdminService;
import garlicbears.quiz.domain.management.common.dto.LoginDto;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import garlicbears.quiz.global.jwt.util.JwtTokenizer;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private static final Logger logger = Logger.getLogger(AdminController.class.getName());
	private final AdminService adminService;
	private final JwtTokenizer jwtTokenizer;
	private final PasswordEncoder passwordEncoder;

	public AdminController(AdminService adminService,
		JwtTokenizer jwtTokenizer,
		PasswordEncoder passwordEncoder) {
		this.adminService = adminService;
		this.jwtTokenizer = jwtTokenizer;
		this.passwordEncoder = passwordEncoder;
	}

	// 관리자 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
		bindingResult.getAllErrors().forEach(error -> logger.warning(error.toString()));
		if (bindingResult.hasErrors()) {

			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		Admin admin = adminService.findByEmail(loginDto.getEmail());
		if (!passwordEncoder.matches(loginDto.getPassword(), admin.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		List<String> roles = admin.getRoles().stream()
			.map(Role::getRoleName)
			.toList();

		String accessToken = jwtTokenizer.createAccessToken(admin.getAdminEmail(), admin.getAdminId(), roles);
		String refreshToken = jwtTokenizer.createAccessToken(admin.getAdminEmail(), admin.getAdminId(), roles);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Access-Token", accessToken);
		headers.set("Refresh-Token", refreshToken);

		return ResponseEntity.ok().headers(headers).body(ResponseDto.success());
	}
}
