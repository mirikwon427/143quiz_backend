package garlicbears._quiz.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import garlicbears._quiz.domain.user.service.RatingService;

@RestController
@RequestMapping("/admin/stat")
public class AdminStatController {
	private final RatingService ratingService;

	public AdminStatController(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	@GetMapping("/rating")
	public ResponseEntity<?> getRatingStat() {
		return ResponseEntity.ok(ratingService.getRatingStat());
	}
}
