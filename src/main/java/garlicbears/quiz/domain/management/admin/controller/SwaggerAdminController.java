package garlicbears.quiz.domain.management.admin.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.management.admin.dto.RequestChangeRoleDto;
import garlicbears.quiz.domain.management.admin.dto.ResponseAdminDto;
import garlicbears.quiz.domain.management.admin.dto.ResponseAdminListDto;
import garlicbears.quiz.domain.management.common.dto.LoginDto;
import garlicbears.quiz.domain.management.common.dto.ResponseUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface SwaggerAdminController {
	@Operation(summary = "관리자 로그인", description = "email, password를 입력 받아 로그인을 시도합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
		@ApiResponse(responseCode = "400", description = "Invalid input",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	public ResponseEntity<?> login(@RequestHeader("User-Agent") String userAgent, @Valid @RequestBody LoginDto loginDto, BindingResult bindingResult,
		HttpServletResponse response);

	@Operation(summary = "Reissue Token", description = "Access Token 만료 시 재발급")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved",
			content = @Content(schema = @Schema(implementation = ResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Invalid refresh token",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@GetMapping("/reissue")
	public ResponseEntity<?> requestRefresh(@RequestHeader("User-Agent") String userAgent, HttpServletRequest request, HttpServletResponse response);

	@Operation(summary = "관리자 로그아웃", description = "로그아웃 처리")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved"),
		@ApiResponse(responseCode = "400", description = "Invalid request"),
		@ApiResponse(responseCode = "500", description = "Internal server error")})
	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);

	@Operation(summary = "관리자 목록 조회", description = "정렬 기준에 따라 관리자 목록을 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved",
			content = {@Content(schema = @Schema(implementation = ResponseAdminListDto.class))}),
		@ApiResponse(responseCode = "401", description = "Unauthorized (Invalid token)")
	})
	public ResponseEntity<?> listAdmins(
		@RequestParam(defaultValue = "createdAtDesc") String sort,
		@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize
	);

	@Operation(summary = "관리자 권한 변경", description = "관리자의 권한을 변경합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved",
			content = {@Content(schema = @Schema(implementation = ResponseAdminDto.class))}),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST (Missing request body variable)"),
		@ApiResponse(responseCode = "401", description = "Unauthorized (Invalid token)"),
		@ApiResponse(responseCode = "404", description = "Not Found (Role Not Found)")
	})
	public ResponseEntity<?> changeAdminRole(
		@Valid @RequestBody RequestChangeRoleDto requestChangeRoleDto
	);

	@Operation(summary = "관리자 삭제", description = "입력된 관리자를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved",
			content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
			content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "404", description = "Admin Not Found",
			content = {@Content(schema = @Schema(implementation = ResponseDto.class))})
	})
	public ResponseEntity<?> deleteAdmin(
		@PathVariable long adminId
	);
}
