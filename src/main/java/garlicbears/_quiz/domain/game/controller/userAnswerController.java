package garlicbears._quiz.domain.game.controller;

import garlicbears._quiz.domain.game.dto.RequestUserAnswerDto;
import garlicbears._quiz.domain.game.dto.ResponseUserAnswerDto;
import garlicbears._quiz.domain.game.service.UserAnswerService;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.global.config.auth.PrincipalDetails;
import garlicbears._quiz.global.dto.ResponseDto;
import garlicbears._quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/game")
@Tag(name = "사용자 답변")
public class userAnswerController {

    private final UserAnswerService userAnswerService;

    @Autowired
    userAnswerController(UserAnswerService userAnswerService) {
        this.userAnswerService = userAnswerService;
    }

    @PostMapping("/answer")
    @Operation(summary = "사용자 게임 답변", description = "게임 종료 후 사용자 답변 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = RequestUserAnswerDto.class))}),
            @ApiResponse(responseCode = "400", description = "MISSING_REQUEST_BODY_VARIABLE",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "HEARTS_COUNT_EXCEEDSS",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "UNKNOWN_TOPIC",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "UNKNOWN_GAMESESSION",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "UNKNOWN_QUESTION",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))})

    })
    public ResponseEntity<?> userAnswerSave(@Valid @RequestBody RequestUserAnswerDto requestUserAnswerDto,
                                            BindingResult bindingResult,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (bindingResult.hasErrors()) {
            ErrorCode errorCode = ErrorCode.MISSING_REQUEST_BODY_VARIABLE;
            ResponseDto<String> response = new ResponseDto<>(errorCode.getStatus().value(), errorCode.toString());
            return new ResponseEntity<>(response, errorCode.getStatus());
        }

        User user  = principalDetails.getUser();

        userAnswerService.userAnswerSave(user, requestUserAnswerDto);
        return ResponseEntity.ok(userAnswerService.rewardUpdate(user, requestUserAnswerDto));
    }

    @GetMapping("/answerDrop/{sessionId}")
    @Operation(summary = "사용자 게임 중도 이탈", description = "사용자가 게임을 중도 이탈했을 경우")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "UNKNOWN_GAMESESSION",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))})

    })
    public ResponseEntity<?> userAnswerDrop(@PathVariable(value = "sessionId") Long sessionId,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User user  = principalDetails.getUser();

        userAnswerService.dropGameSession(sessionId, user);

        return ResponseEntity.ok(ResponseDto.success());

    }

}
