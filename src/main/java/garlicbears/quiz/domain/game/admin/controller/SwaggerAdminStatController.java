package garlicbears.quiz.domain.game.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import garlicbears.quiz.domain.game.admin.dto.RatingStatDto;
import garlicbears.quiz.domain.game.admin.dto.ResponseTopicListDto;
import garlicbears.quiz.domain.game.admin.dto.TotalVisitorCountDto;
import garlicbears.quiz.domain.game.admin.dto.VisitorCountListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface SwaggerAdminStatController {
	@Operation(summary = "평가 통계 조회", description = "평가 통계 정보를 반환합니다. 평균 평점, 평가 횟수")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(schema = @Schema(implementation = RatingStatDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")})
	public ResponseEntity<?> getRatingStat();

	@Operation(summary = "게임 통계 조회", description = "정렬 기준에 따라 게임 통계 정보를 반환합니다."
		+ "주제별 플레이 횟수, 정상 종료 수, 평균 플레이 시간, 평균 정답률")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(schema = @Schema(implementation = ResponseTopicListDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")})
	public ResponseEntity<?> getGameStatList(
		@RequestParam(defaultValue = "createdAtDesc") @Parameter(schema = @Schema(allowableValues = {"titleDesc",
		"titleAsc", "createdAtDesc", "createdAtAsc", "updatedAtDesc", "updatedAtAsc", "usageCountDesc",
		"usageCountAsc"})) String sort, @RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize);

	@Operation(summary = "총 방문자 수 조회", description = "총 방문자 수를 반환합니다. (누적) 일일 방문자 수")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(schema = @Schema(implementation = TotalVisitorCountDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")})
	public ResponseEntity<?> getTotalVisitors();

	@Operation(summary = "일별 방문자 수 조회", description = "일별 방문자 수 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(schema = @Schema(implementation = VisitorCountListDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")})
	public ResponseEntity<?> getDailyVisitors(@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize);

	@Operation(summary = "주별 방문자 수 조회", description = "주별 방문자 수 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(schema = @Schema(implementation = VisitorCountListDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")})
	public ResponseEntity<?> getWeeklyVisitors(@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize);

	@Operation(summary = "월별 방문자 수 조회", description = "월별 방문자 수 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(schema = @Schema(implementation = VisitorCountListDto.class))}),
		@ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")})
	public ResponseEntity<?> getMonthlyVisitors(@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize);
}
