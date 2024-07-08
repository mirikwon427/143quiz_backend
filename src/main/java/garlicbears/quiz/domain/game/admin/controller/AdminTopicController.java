package garlicbears.quiz.domain.game.admin.controller;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.domain.common.dto.ResponseImageDto;
import garlicbears.quiz.domain.common.entity.Image;
import garlicbears.quiz.domain.common.service.ImageService;
import garlicbears.quiz.domain.game.admin.service.TopicService;
import garlicbears.quiz.domain.game.common.entity.Topic;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@Tag(name = "주제 관리")
public class AdminTopicController implements SwaggerAdminTopicController {
	private static Logger logger = LoggerFactory.getLogger(AdminTopicController.class);
	private final TopicService topicService;
	private final ImageService imageService;

	@Autowired
	public AdminTopicController(TopicService topicService,
		ImageService imageService) {
		this.topicService = topicService;
		this.imageService = imageService;
	}

	@GetMapping("/topics")
	public ResponseEntity<?> listTopics(
		@RequestParam(defaultValue = "createdAtDesc") @Parameter(schema = @Schema(allowableValues = {"titleDesc",
			"titleAsc", "createdAtDesc", "createdAtAsc", "updatedAtDesc", "updatedAtAsc", "usageCountDesc",
			"usageCountAsc", "questionCountAsc", "questionCountDesc"})) String sort, @RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize) {
		// 정렬 조건에 따른 전체 주제 목록 조회
		return ResponseEntity.ok(topicService.getTopicList(pageNumber, pageSize, sort));
	}

	@PostMapping(value = "/topic/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createTopicsByExcel(
		@Parameter(description = "multipart/form-data 형식의 엑셀 파일을 받습니다.",
			content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
		@RequestPart("excel") MultipartFile file) {
		// 엑셀 파일을 통해 주제 생성
		// 파일명 = 주제명, 첫 번째 시트만 사용, 첫 번째 열 = 주제명
		return ResponseEntity.ok(topicService.saveTopicWithExcel(file));
	}

	@PostMapping("/topic")
	public ResponseEntity<?> createTopic(@Valid @RequestBody Map<String, String> request) {
		// 주제 생성
		String topicTitle = request.get("title");

		if (topicTitle == null || topicTitle.trim().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		topicService.save(topicTitle);

		return ResponseEntity.ok(ResponseDto.success());
	}

	@PatchMapping("/topic/{topicId}")
	public ResponseEntity<?> updateTopic(@PathVariable Long topicId, @Valid @RequestBody Map<String, String> request) {
		// 주제 수정
		String topicTitle = request.get("title");

		if (topicTitle == null || topicTitle.trim().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}

		topicService.update(topicId, topicTitle);

		return ResponseEntity.ok(ResponseDto.success());
	}

	@DeleteMapping("/topic/{topicId}")
	public ResponseEntity<?> deleteTopic(@PathVariable Long topicId) {
		// 주제 삭제
		topicService.delete(topicId);

		return ResponseEntity.ok(ResponseDto.success());
	}

	@PostMapping("/topic/{topicId}/image")
	public ResponseEntity<?> uploadTopicImage(
		@PathVariable Long topicId, @ModelAttribute MultipartFile image) {

		Optional<Topic> topic = topicService.findByTopicId(topicId);
		if (topic.isPresent()) {
			Image newImage = imageService.processImage(topic.get(), image, null);
			topicService.updateImage(topic.get(), newImage);
			// 이미지 URL을 JSON 형식으로 반환
			ResponseImageDto responseImageDto = new ResponseImageDto(newImage.getAccessUrl());
			return ResponseEntity.ok(responseImageDto);
		} else {
			logger.error("해당 topicId의 주제가 없습니다");
			throw new CustomException(ErrorCode.TOPIC_NOT_FOUND);
		}
	}
}
