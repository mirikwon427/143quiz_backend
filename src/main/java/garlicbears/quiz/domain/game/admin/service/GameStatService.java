package garlicbears.quiz.domain.game.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import garlicbears.quiz.domain.common.repository.RatingRepository;
import garlicbears.quiz.domain.game.admin.dto.RatingStatDto;

@Service
public class GameStatService {
	private final RatingRepository ratingRepository;

	@Autowired
	public GameStatService(RatingRepository ratingRepository) {
		this.ratingRepository = ratingRepository;
	}

	public RatingStatDto getRatingStat() {
		// 별점 평균 반환
		Double averageRating = ratingRepository.getAverageRating();
		if (averageRating == null) {
			return new RatingStatDto(0, 0);
		}
		return new RatingStatDto(averageRating, ratingRepository.count());
	}
}
