package garlicbears._quiz.domain.admin.controller;

import garlicbears._quiz.domain.admin.dto.CreateQuestionsDto;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.domain.game.service.QuestionService;
import garlicbears._quiz.domain.game.service.TopicService;
import garlicbears._quiz.domain.user.dto.ResponseUserDto;
import garlicbears._quiz.global.dto.ResponseDto;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@Tag(name = "문제 관리")
public class AdminQuestionController {
    private final TopicService topicService;
    private final QuestionService questionService;

    @Autowired
    public AdminQuestionController(TopicService topicService, QuestionService questionService) {
        this.topicService = topicService;
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    @Operation(summary = "문제 목록 조회", description = "정렬 기준에 따라 문제 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")
    })
    public ResponseEntity<?> listQuestions(@RequestParam(defaultValue = "createdAtDesc") @Parameter(schema =
    @Schema(allowableValues = {"titleDesc", "titleAsc", "createdAtDesc", "createdAtAsc", "updatedAtDesc", "updatedAtAsc"})) String sort
            , @RequestParam(defaultValue = "0") int pageNumber
            , @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(questionService.getQuestionList(pageNumber, pageSize, sort));
    }

    @GetMapping("/topic/{topicId}/questions")
    @Operation(summary = "주제별 문제 목록 조회", description = "주제별 문제 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)"),
            @ApiResponse(responseCode = "404", description = "Topic Not Found",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))})
    })
    public ResponseEntity<?> listQuestionsByTopic(@PathVariable(value="topicId") long topicId,
                                                  @RequestParam(defaultValue = "createdAtDesc") @Parameter(schema =
                                                  @Schema(allowableValues = {"titleDesc", "titleAsc", "createdAtDesc", "createdAtAsc", "updatedAtDesc", "updatedAtAsc"})) String sort
            , @RequestParam(defaultValue = "0") int pageNumber
            , @RequestParam(defaultValue = "10") int pageSize) {
        Optional<Topic> topic = topicService.findByTopicId(topicId);
        if (topic.isEmpty()) {
            throw new CustomException(ErrorCode.TOPIC_NOT_FOUND);
        }

        return ResponseEntity.ok(questionService.getQuestionListByTopic(topic.get(), pageNumber, pageSize, sort));
    }

    @PostMapping("/topic/{topicId}/question")
    @Operation(summary = "문제 생성", description = "입력된 문제를 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "title", schema = @Schema(type="string", format = "json"))
                            }
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Input",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Topic Not Found",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "Question Already Exist",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "410", description = "Deleted Topic",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
    })
    public ResponseEntity<?> createQuestion(@PathVariable(value="topicId") long topicId,
                                            @Valid @RequestBody Map<String, String> request) {
        String questionText = request.get("title");

        if (questionText == null || questionText.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        Optional<Topic> topic = topicService.findByTopicId(topicId);
        if (topic.isEmpty()) {
            throw new CustomException(ErrorCode.TOPIC_NOT_FOUND);
        }
        if (topic.get().getTopicActive() == Active.inactive) {
            throw new CustomException(ErrorCode.DELETED_TOPIC);
        }

        questionService.save(topic.get(), questionText);

        return ResponseEntity.ok(ResponseDto.success());
    }

    @PostMapping("/questions")
    public ResponseEntity<?> createQuestions(@Valid @RequestBody CreateQuestionsDto request)
    {
        Topic topicEntity = null;
        for (Topic topic : topicService.findByTopicTitle(request.getTopicTitle())) {
            if (topic.getTopicActive() == Active.active) {
                topicEntity = topic;
                break;
            }
        }
        if (topicEntity == null) {
            throw new CustomException(ErrorCode.TOPIC_NOT_FOUND);
        }

        for (String questionText : request.getQuestions()) {
            if (questionText == null || questionText.trim().isEmpty()) {
                continue;
            }
            try {
                questionService.save(topicEntity, questionText);
            }catch(Exception e){
                System.out.println(questionText + " " + e.getMessage());
            }
        }

        return ResponseEntity.ok(ResponseDto.success());
    }

    @PatchMapping("/question/{questionId}")
    @Operation(summary = "문제 수정", description = "입력된 문제를 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "title", schema = @Schema(type="string", format = "json"))
                            }
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "Question Not Found",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))})
    })
    public ResponseEntity<?> updateQuestion(@PathVariable(value="questionId") long questionId,
                                            @Valid @RequestBody Map<String, String> request) {
        String questionText = request.get("title");

        if (questionText == null || questionText.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        questionService.update(questionId, questionText);

        return ResponseEntity.ok(ResponseDto.success());
    }

    @DeleteMapping("/question/{questionId}")
    @Operation(summary = "문제 삭제", description = "입력된 문제를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "Question Not Found",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))})
    })
    public ResponseEntity<?> deleteQuestion(@PathVariable(value="questionId") long questionId) {
        questionService.delete(questionId);

        return ResponseEntity.ok(ResponseDto.success());
    }
}
