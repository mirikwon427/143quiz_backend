package garlicbears.quiz.domain.management.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import garlicbears.quiz.domain.common.entity.Rating;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.repository.RatingRepository;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Service
public class UserRatingService {
	private final RatingRepository ratingRepository;

	@Autowired
	public UserRatingService(RatingRepository ratingRepository) {
		this.ratingRepository = ratingRepository;
	}

	public void saveRating(User user, Double ratingValue) {
		// 이미 평가한 사용자인지 확인
		ratingRepository.findByUser(user).ifPresent(rating -> {
			throw new CustomException(ErrorCode.ALREADY_RATED);
		});

		// 별점 값이 0~5 사이인지 확인
		if (ratingValue == null || ratingValue < 0 || ratingValue > 5) {
			throw new CustomException(ErrorCode.ILLEGAL_RATING_VALUE);
		}

		ratingRepository.save(new Rating(user, ratingValue));
	}
}
