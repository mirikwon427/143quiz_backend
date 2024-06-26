package garlicbears.quiz.domain.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import garlicbears.quiz.domain.common.entity.Rating;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.game.admin.repository.AdminRatingQueryRepository;

@Repository
public interface RatingRepository
	extends JpaRepository<Rating, Long>, AdminRatingQueryRepository {
	Optional<Rating> findByUser(User user);
}
