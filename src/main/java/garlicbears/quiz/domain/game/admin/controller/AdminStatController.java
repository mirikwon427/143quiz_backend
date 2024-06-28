package garlicbears.quiz.domain.game.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.game.admin.service.GameStatService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/stat")
@Tag(name = "통계 관리")
public class AdminStatController {
	private final GameStatService gameStatService;

	public AdminStatController(GameStatService gameStatService) {
		this.gameStatService = gameStatService;
	}

	@GetMapping("/rating")
	public ResponseEntity<?> getRatingStat() {
		return ResponseEntity.ok(gameStatService.getRatingStat());
	}

	@GetMapping("/game")
	public ResponseEntity<?> getGameStatList(int pageNumber, int pageSize, String sort) {
		return ResponseEntity.ok(gameStatService.getGameStatList(pageNumber, pageSize, sort));
	}
}
