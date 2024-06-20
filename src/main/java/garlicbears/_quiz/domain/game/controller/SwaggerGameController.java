package garlicbears._quiz.domain.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import garlicbears._quiz.domain.user.dto.UserRankingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface SwaggerGameController {
	@Operation(summary = "게임 랭킹 조회", description = "전체 뱃지 수, 하트 수를 기준으로 랭킹 정보 조회")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = UserRankingDto.class))}),})
	public ResponseEntity<?> getRankings(@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize);

	@Operation(summary = "주제별 게임 랭킹 조회", description = "주제별 하트 수를 기준으로 랭킹 정보 조회")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
		@Content(schema = @Schema(implementation = UserRankingDto.class))}),})
	public ResponseEntity<?> getRankingsByTopicId(@PathVariable(value = "topicId") long topicId,
		@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize);
}
