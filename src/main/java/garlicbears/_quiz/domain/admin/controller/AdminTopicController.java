package garlicbears._quiz.domain.admin.controller;

import garlicbears._quiz.domain.admin.dto.CreateTopicsDto;
import garlicbears._quiz.domain.game.dto.ResponseTopicListDto;
import garlicbears._quiz.domain.game.service.TopicService;
import garlicbears._quiz.domain.user.dto.ResponseUserDto;
import garlicbears._quiz.global.dto.ResponseDto;
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

@RestController
@RequestMapping("/admin")
@Tag(name = "주제 관리")
public class AdminTopicController {
    private final TopicService topicService;

    @Autowired
    public AdminTopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/topics")
    @Operation(summary = "주제 목록 조회", description = "정렬 기준에 따라 주제 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseTopicListDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")
    })
    public ResponseEntity<?> listTopics(@RequestParam(defaultValue = "createdAtDesc") @Parameter(schema =
    @Schema(allowableValues = {"titleDesc", "titleAsc", "createdAtDesc", "createdAtAsc", "updatedAtDesc", "updatedAtAsc", "usageCountDesc", "usageCountAsc"})) String sort
            , @RequestParam(defaultValue = "0") int pageNumber
            , @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(topicService.getTopicList(pageNumber, pageSize, sort));
    }

    @PostMapping("/topic")
    @Operation(summary = "주제 생성", description = "입력된 주제를 생성합니다.",
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
            @ApiResponse(responseCode = "409", description = "Nickname Already Exist",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
    })
    public ResponseEntity<?> createTopic(@Valid @RequestBody Map<String, String> request) {
        String topicTitle = request.get("title");

        if (topicTitle == null || topicTitle.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        topicService.save(topicTitle);

        return ResponseEntity.ok(ResponseDto.success());
    }

    @PostMapping("/topics")
    public ResponseEntity<?> createTopics(@Valid @RequestBody CreateTopicsDto request) {
        for (String topicTitle : request.getTopics()) {
            if (topicTitle == null || topicTitle.trim().isEmpty()) {
                continue;
            }

            try {
                topicService.save(topicTitle);
            } catch (CustomException e) {
                System.err.println(e.getMessage());
            }
        }

        return ResponseEntity.ok(ResponseDto.success());
    }

    @PatchMapping("/topic/{topicId}")
    @Operation(summary = "주제 수정", description = "입력된 주제를 수정합니다.",
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
            @ApiResponse(responseCode = "404", description = "Topic Not Found",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))})
    })
    public ResponseEntity<?> updateTopic(@PathVariable Long topicId, @Valid @RequestBody Map<String, String> request) {
        String topicTitle = request.get("title");

        if (topicTitle == null || topicTitle.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        topicService.update(topicId, topicTitle);

        return ResponseEntity.ok(ResponseDto.success());
    }

    @DeleteMapping("/topic/{topicId}")
    @Operation(summary = "주제 삭제", description = "입력된 주제를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "Topic Not Found",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))})
    })
    public ResponseEntity<?> deleteTopic(@PathVariable Long topicId) {
        topicService.delete(topicId);

        return ResponseEntity.ok(ResponseDto.success());
    }

}
