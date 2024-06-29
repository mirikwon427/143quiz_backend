package garlicbears.quiz.domain.game.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.game.admin.service.TopicService;
import garlicbears.quiz.domain.game.user.dto.ResponseTopicBadgeDto;
import garlicbears.quiz.domain.game.user.dto.TopicsListDto;
import garlicbears.quiz.domain.game.user.service.GameService;
import garlicbears.quiz.domain.management.user.service.UserService;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/game")
@Tag(name = "게임")
public class GameController implements SwaggerGameController {

	private final GameService gameService;
	private final TopicService topicService;
	private final UserService userService;

	public GameController(GameService gameService, TopicService topicService,
		UserService userService) {
		this.gameService = gameService;
		this.topicService = topicService;
		this.userService = userService;

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
	public ResponseEntity<?> topicList(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		// 현재 인증된 사용자의 정보를 userDetails로부터 가져올 수 있습니다.
		User user = userService.findByEmail(userDetails.getUsername());
		List<TopicsListDto> topics = gameService.topicList(user.getUserId());

		return ResponseEntity.ok(new ResponseTopicBadgeDto(topics));
	}

	/**
	 * 뱃지 주제 목록
	 */
	@GetMapping("/badges")
	public ResponseEntity<?> badges(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		// 현재 인증된 사용자의 정보를 userDetails로부터 가져올 수 있습니다.
		User user = userService.findByEmail(userDetails.getUsername());

		List<TopicsListDto> topics = gameService.badgeList(user.getUserId());

		return ResponseEntity.ok(new ResponseTopicBadgeDto(topics));
	}

	/**
	 * 게임 주제 선택
	 */
	@GetMapping("/start/{topicId}")
	public ResponseEntity<?> gameStart(@PathVariable(value = "topicId") long topicId,
		@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		// 현재 인증된 사용자의 정보를 userDetails로부터 가져올 수 있습니다.
		User user = userService.findByEmail(userDetails.getUsername());

		return ResponseEntity.ok(gameService.gameStart(topicId, user));
	}
}
