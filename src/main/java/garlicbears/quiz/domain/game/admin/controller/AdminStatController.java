package garlicbears.quiz.domain.game.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.game.admin.service.AdminRatingService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/stat")
@Tag(name = "통계 관리")
public class AdminStatController {
	private final AdminRatingService adminRatingService;

	public AdminStatController(AdminRatingService adminRatingService) {
		this.adminRatingService = adminRatingService;
	}

	@GetMapping("/rating")
	public ResponseEntity<?> getRatingStat() {
		return ResponseEntity.ok(adminRatingService.getRatingStat());
	}
}
