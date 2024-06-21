package garlicbears._quiz.domain.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import garlicbears._quiz.domain.game.service.GameService;
import garlicbears._quiz.domain.game.service.TopicService;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/game")
@Tag(name = "게임 관리")
public class GameController implements SwaggerGameController {

	private final GameService gameService;
	private final TopicService topicService;

	public GameController(GameService gameService, TopicService topicService) {
		this.gameService = gameService;
		this.topicService = topicService;
	}

	@GetMapping("/rankings")
	public ResponseEntity<?> getRankings(@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize) {

		return ResponseEntity.ok(gameService.getRankings(pageNumber, pageSize));
	}

	@GetMapping("/rankings/{topicId}")
	public ResponseEntity<?> getRankingsByTopicId(@PathVariable(value = "topicId") long topicId,
		@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {

		topicService.findByTopicId(topicId).orElseThrow(() -> new CustomException(ErrorCode.TOPIC_NOT_FOUND));

		return ResponseEntity.ok(gameService.getRankingsByTopicId(topicId, pageNumber, pageSize));
	}
}
