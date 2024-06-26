package garlicbears.quiz.domain.management.admin.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.management.admin.dto.AdminSignUpDto;
import garlicbears.quiz.domain.management.admin.dto.ResponseAdminDto;
import garlicbears.quiz.domain.management.admin.dto.ResponseAdminListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

public interface SwaggerAdminController {
	@Operation(summary = "email 중복 확인", description = "입력된 이메일이 이미 가입된 유저인지 확인합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schemaProperties = {
		@SchemaProperty(name = "email", schema = @Schema(type = "string", format = "json"))})))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "409", description = "Email Already Exist", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))}),})
	public ResponseEntity<?> checkEmail(@Valid @RequestBody Map<String, String> request);

	@Operation(summary = "회원가입", description = "입력받은 정보를 바탕으로 회원을 생성합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseAdminDto.class))}),
		@ApiResponse(responseCode = "400", description = "Bad Request", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))})})
	public ResponseEntity<?> signUp(@Valid @RequestBody AdminSignUpDto request,
		BindingResult bindingResult);

	@Operation(summary = "관리자 목록 조회", description = "정렬 기준에 따라 관리자 목록을 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved",
			content = {@Content(schema = @Schema(implementation = ResponseAdminListDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")
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
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)"),
		@ApiResponse(responseCode = "404", description = "Not Found (Admin not found)")
	})
	public ResponseEntity<?> changeAdminRole(
		@RequestParam(value = "adminId") long adminId
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
		@RequestParam(value = "adminId") long adminId
	);
}
