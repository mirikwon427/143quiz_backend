package garlicbears._quiz.domain.admin.controller;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import garlicbears._quiz.domain.admin.dto.AdminSignUpDto;
import garlicbears._quiz.domain.admin.service.AdminService;
import garlicbears._quiz.domain.user.controller.UserController;
import garlicbears._quiz.global.dto.ResponseDto;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@Tag(name = "관리자 회원 관리")
public class AdminController implements SwaggerAdminController {
	private static final Logger logger = Logger.getLogger(UserController.class.getName());
	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping("/checkEmail")
	public ResponseEntity<?> checkEmail(@Valid @RequestBody Map<String, String> request) {
		String email = request.get("email");
		if (email == null || email.trim().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
		adminService.checkDuplicatedEmail(email);
		return ResponseEntity.ok(ResponseDto.success());
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody AdminSignUpDto request,
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
		adminService.checkDuplicatedEmail(request.getEmail());
		adminService.signUp(request);
		return ResponseEntity.ok(ResponseDto.success());
	}
}
