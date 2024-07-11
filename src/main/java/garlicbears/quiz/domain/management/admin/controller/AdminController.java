package garlicbears.quiz.domain.management.admin.controller;

import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.common.entity.Admin;
import garlicbears.quiz.domain.common.entity.Role;
import garlicbears.quiz.domain.management.admin.dto.RequestChangeRoleDto;
import garlicbears.quiz.domain.management.admin.service.AdminService;
import garlicbears.quiz.domain.management.common.dto.LoginDto;
import garlicbears.quiz.domain.management.common.service.AuthService;
import garlicbears.quiz.domain.management.common.service.RoleService;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@Tag(name = "관리자 회원 관리")
public class AdminController implements SwaggerAdminController {
	private static final Logger logger = Logger.getLogger(AdminController.class.getName());
	private final AdminService adminService;
	private final PasswordEncoder passwordEncoder;
	private final AuthService authService;
	private final RoleService roleService;


	public AdminController(AdminService adminService,
		PasswordEncoder passwordEncoder,
		AuthService authService,
		RoleService roleService) {
		this.adminService = adminService;
		this.passwordEncoder = passwordEncoder;
		this.authService = authService;
		this.roleService = roleService;
	}

	/**
	 * 로그인
	 * 유효한 관리자 정보를 전달받아 액세스 토큰과 리프레시 토큰을 발급.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestHeader("User-Agent") String userAgent, @Valid @RequestBody LoginDto loginDto, BindingResult bindingResult,
		HttpServletResponse response) {

		if (bindingResult.hasErrors()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		Admin admin = adminService.findByEmail(loginDto.getEmail());
		if (!passwordEncoder.matches(loginDto.getPassword(), admin.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		return authService.createTokensAndRespond(userAgent, admin, response);
	}

	/**
	 * 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급.
	 */
	@GetMapping("/reissue")
	public ResponseEntity<?> requestRefresh(@RequestHeader("User-Agent") String userAgent, HttpServletRequest request, HttpServletResponse response) {
		String accessToken = authService.requestRefresh(userAgent, request, response);

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

	/**
	 * 관리자 목록 조회
	 */
	@Override
	@GetMapping("/")
	public ResponseEntity<?> listAdmins(
		@RequestParam(defaultValue = "createdAtDesc") String sort,
		@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize) {
		return ResponseEntity.ok(adminService.getAdminList(pageNumber, pageSize, sort));
	}

	/**
	 * 관리자 권한 변경
	 */
	@Override
	@PatchMapping("/changeRole")
	public ResponseEntity<?> changeAdminRole(@Valid @RequestBody RequestChangeRoleDto requestChangeRoleDto) {
		Admin admin = adminService.findById(requestChangeRoleDto.getAdminId());
		Role adminRole = roleService.findByRoleName(requestChangeRoleDto.getRoleName());
		adminService.updateRole(admin, adminRole);
		return ResponseEntity.ok(ResponseDto.success());
	}

	/**
	 * 관리자 삭제
	 */
	@DeleteMapping("/delete/{adminId}")
	public ResponseEntity<?> deleteAdmin(@PathVariable long adminId) {
		adminService.delete(adminId);
		return ResponseEntity.ok(ResponseDto.success());
	}
}
