package garlicbears._quiz.domain.admin.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import garlicbears._quiz.domain.admin.dto.AdminSignUpDto;
import garlicbears._quiz.domain.admin.dto.ResponseAdminDto;
import garlicbears._quiz.global.dto.ResponseDto;
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
}
