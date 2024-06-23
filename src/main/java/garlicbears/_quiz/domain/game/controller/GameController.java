package garlicbears._quiz.domain.game.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import garlicbears._quiz.domain.game.dto.ResponseTopicBadgeDto;
import garlicbears._quiz.domain.game.dto.TopicsListDto;
import garlicbears._quiz.domain.game.service.GameService;
import garlicbears._quiz.domain.game.service.TopicService;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.global.config.auth.PrincipalDetails;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;

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

	/**
	 * 게임 주제 목록 ( 뱃지 미획득 )
	 */
	@GetMapping("/topics")
	public ResponseEntity<?> topicList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		User user = principalDetails.getUser();
		List<TopicsListDto> topics = gameService.topicList(user.getUserId());

		return ResponseEntity.ok(new ResponseTopicBadgeDto(topics));
	}

	/**
	 * 뱃지 주제 목록
	 */
	@GetMapping("/badges")
	public ResponseEntity<?> badges(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		User user = principalDetails.getUser();
		List<TopicsListDto> topics = gameService.badgeList(user.getUserId());

		return ResponseEntity.ok(new ResponseTopicBadgeDto(topics));
	}

	/**
	 * 게임 주제 선택
	 */
	@GetMapping("/start/{topicId}")
	public ResponseEntity<?> gameStart(@PathVariable(value = "topicId") long topicId,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		User user = principalDetails.getUser();

		return ResponseEntity.ok(gameService.gameStart(topicId, user));
	}
}
