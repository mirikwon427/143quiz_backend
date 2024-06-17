package garlicbears._quiz.domain.user.service;

import garlicbears._quiz.domain.game.dto.RatingStatDto;
import garlicbears._quiz.domain.user.entity.Rating;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.domain.user.repository.RatingRepository;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public RatingStatDto getRatingStat() {
        Double averageRating = ratingRepository.getAverageRating();
        if (averageRating == null) {
            return new RatingStatDto(0, 0);
        }
        return new RatingStatDto(averageRating, ratingRepository.count());
    }

    public void saveRating(User user, Double ratingValue) {
        ratingRepository.findByUser(user).ifPresent(rating -> {
            throw new CustomException(ErrorCode.ALREADY_RATED);
        });

        if (ratingValue == null || ratingValue < 0 || ratingValue > 5) {
            throw new CustomException(ErrorCode.ILLEGAL_RATING_VALUE);
        }

        ratingRepository.save(new Rating(user, ratingValue));
    }
}
