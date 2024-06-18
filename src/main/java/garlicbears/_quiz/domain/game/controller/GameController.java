package garlicbears._quiz.domain.game.controller;

import garlicbears._quiz.domain.game.service.GameService;
import garlicbears._quiz.domain.game.service.TopicService;
import garlicbears._quiz.domain.user.dto.UserRankingDto;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final TopicService topicService;

    public GameController(GameService gameService, TopicService topicService) {
        this.gameService = gameService;
        this.topicService = topicService;
    }

    @GetMapping("/rankings")
    @Operation(summary = "게임 랭킹 조회", description = "전체 뱃지 수, 하트 수를 기준으로 랭킹 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = UserRankingDto.class))}),
    })
    public ResponseEntity<?> getRankings(@RequestParam(defaultValue = "0") int pageNumber
            , @RequestParam(defaultValue = "10") int pageSize) {

        return ResponseEntity.ok(gameService.getRankings(pageNumber, pageSize));
    }

    @GetMapping("/rankings/{topicId}")
    @Operation(summary = "주제별 게임 랭킹 조회", description = "주제별 하트 수를 기준으로 랭킹 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = UserRankingDto.class))}),
    })
    public ResponseEntity<?> getRankingsByTopicId(@PathVariable(value="topicId") long topicId,
                                                  @RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize) {

        topicService.findByTopicId(topicId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNKNOWN_TOPIC));

        return ResponseEntity.ok(gameService.getRankingsByTopicId(topicId, pageNumber, pageSize));
    }
}
