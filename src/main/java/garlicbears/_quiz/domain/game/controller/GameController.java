package garlicbears._quiz.domain.game.controller;

import garlicbears._quiz.domain.game.dto.ResponseTopicBadgeDto;
import garlicbears._quiz.domain.game.dto.TopicsListDto;
import garlicbears._quiz.domain.game.service.GameService;
import garlicbears._quiz.domain.game.service.TopicService;
import garlicbears._quiz.domain.user.dto.UserRankingDto;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.global.config.auth.PrincipalDetails;
import garlicbears._quiz.global.dto.ResponseDto;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@Tag(name = "게임")
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
                .orElseThrow(() -> new CustomException(ErrorCode.TOPIC_NOT_FOUND));

        return ResponseEntity.ok(gameService.getRankingsByTopicId(topicId, pageNumber, pageSize));
    }
    @GetMapping("/topics")
    @Operation(summary = "주제 조회", description = "게임 시작 전 뱃지 미획득 주제 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseTopicBadgeDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
    })
    public ResponseEntity<?> topics (@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        List<TopicsListDto> topics = gameService.topicList(user.getUserId());

        return ResponseEntity.ok(new ResponseTopicBadgeDto(200, topics));
    }

    @GetMapping("/badges")
    @Operation(summary = "뱃지 조회", description = "획득한 뱃지 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseTopicBadgeDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))})
    })
    public ResponseEntity<?> badges (@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        List<TopicsListDto> topics = gameService.badgeList(user.getUserId());

        return ResponseEntity.ok(new ResponseTopicBadgeDto(200, topics));
    }

    @GetMapping("/start/{topicId}")
    @Operation(summary = "게임 시작", description = "주제를 선택한 후 게임 시작.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseTopicBadgeDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "UNKNOWN_TOPIC",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))})
    })
    public ResponseEntity<?> gameStart(@PathVariable(value = "topicId") Long topicId,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();

        return ResponseEntity.ok(gameService.gameStart(topicId, user));
    }
}
