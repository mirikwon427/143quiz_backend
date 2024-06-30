package garlicbears.quiz.domain.game.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.game.admin.service.GameStatService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/stat")
@Tag(name = "통계 관리")
public class AdminStatController implements SwaggerAdminStatController{
	private final GameStatService gameStatService;

	public AdminStatController(GameStatService gameStatService) {
		this.gameStatService = gameStatService;
	}

	@GetMapping("/rating")
	public ResponseEntity<?> getRatingStat() {
		return ResponseEntity.ok(gameStatService.getRatingStat());
	}

	/**
	 * 게임 통계 리스트 조회
	 */
	@GetMapping("/game")
	public ResponseEntity<?> getGameStatList(@RequestParam(defaultValue = "createdAtDesc") @Parameter(schema = @Schema(allowableValues = {"titleDesc",
		"titleAsc", "createdAtDesc", "createdAtAsc", "updatedAtDesc", "updatedAtAsc", "usageCountDesc",
		"usageCountAsc"})) String sort, @RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize) {
		return ResponseEntity.ok(gameStatService.getGameStatList(pageNumber, pageSize, sort));
	}

	/**
	 * 일일 방문자 수 조회
	 */
	@GetMapping("/dailyVisitors")
	public ResponseEntity<?> getDailyVisitors(@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize) {
		return ResponseEntity.ok(gameStatService.getDailyVisitors(pageNumber, pageSize));
	}

	/**
	 * 주간 방문자 수 조회
	 */
	@GetMapping("/weeklyVisitors")
	public ResponseEntity<?> getWeeklyVisitors(@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize) {
		return ResponseEntity.ok(gameStatService.getWeeklyVisitors(pageNumber, pageSize));
	}

	/**
	 * 월별 방문자 수 조회
	 */
	@GetMapping("/monthlyVisitors")
	public ResponseEntity<?> getMonthlyVisitors(@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize) {
		return ResponseEntity.ok(gameStatService.getMonthlyVisitors(pageNumber, pageSize));
	}
}
