package garlicbears.quiz.domain.management.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import garlicbears.quiz.domain.common.dto.ImageSaveDto;
import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.management.common.dto.LoginDto;
import garlicbears.quiz.domain.management.common.dto.ResponseUserDto;
import garlicbears.quiz.domain.management.user.dto.SignUpDto;
import garlicbears.quiz.domain.management.user.dto.UpdateUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface SwaggerUserController {
	@Operation(summary = "email 중복 확인", description = "입력된 이메일이 이미 가입된 유저인지 확인합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schemaProperties = {
		@SchemaProperty(name = "email", schema = @Schema(type = "string", format = "json"))})))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "409", description = "Email Already Exist", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))}),})
	public ResponseEntity<?> checkEmail(@Valid @RequestBody Map<String, String> request);

	@Operation(summary = "nickname 중복 확인", description = "입력된 닉네임이 이미 가입된 유저인지 확인합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schemaProperties = {
		@SchemaProperty(name = "nickname", schema = @Schema(type = "string", format = "json"))})))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "409", description = "Nickname Already Exist", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))}),})
	public ResponseEntity<?> checkNickname(@Valid @RequestBody Map<String, String> request);

	@Operation(summary = "회원가입", description = "입력받은 정보를 바탕으로 회원을 생성합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
		@ApiResponse(responseCode = "400", description = "Bad Request", content = {
			@Content(schema = @Schema(implementation = ResponseUserDto.class))})})
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto, BindingResult bindingResult);

	@Operation(summary = "유저 정보(내 정보) 조회", description = "jwt token을 받아 유저 정보를 반환합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")})
	public ResponseEntity<?> searchUser(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request);

	@Operation(summary = "유저 정보(내 정보) 수정", description = "jwt token을 받아 유저 정보를 확인. resquest body로 전달받은 정보로 수정합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
		@ApiResponse(responseCode = "400", description = "Bad Request (Invalid Input)", content = {
			@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)", content = {
			@Content(schema = @Schema(implementation = ResponseUserDto.class))})})
	public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody UpdateUserDto updateUserDto);

	@Operation(summary = "계정 탈퇴", description = "jwt token을 받아 유저 정보를 확인. status 를 inactive로 변환합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))})})
	public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails);

	@Operation(summary = "평점 주기", description = "jwt token을 받아 유저 정보를 확인. request body로 평점을 전달받아 저장합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schemaProperties = {
		@SchemaProperty(name = "ratingValue", schema = @Schema(type = "double", format = "json"))})))
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "400", description = "Bad Request (Invalid Input)", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))})})
	public ResponseEntity<?> rating(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody Map<String, Double> request);

	@PatchMapping("/image")
	@Operation(summary = "회원 이미지 업로드", description = "회원 프로필 이미지 업로드")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(schema = @Schema(implementation = String.class))}),
		@ApiResponse(responseCode = "404", description = "User Not Found", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))}),
		@ApiResponse(responseCode = "404", description = "Image Not Found", content = {
			@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
		@ApiResponse(responseCode = "500", description = "Internal server error", content = {
			@Content(schema = @Schema(implementation = ResponseDto.class))})})
	public ResponseEntity<?> updateUserImage(@AuthenticationPrincipal UserDetails userDetails,
		@ModelAttribute ImageSaveDto imageSaveDto);

	@Operation(summary = "로그인", description = "email, password를 입력 받아 로그인을 시도합니다.")
		@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid input",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult,
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
	public ResponseEntity<?> requestRefresh(HttpServletRequest request, HttpServletResponse response);

	@Operation(summary = "로그아웃", description = "로그아웃 처리")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved"),
		@ApiResponse(responseCode = "400", description = "Invalid request"),
		@ApiResponse(responseCode = "500", description = "Internal server error")})
	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);

}
