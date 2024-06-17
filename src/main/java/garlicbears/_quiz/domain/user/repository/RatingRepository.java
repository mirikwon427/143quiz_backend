package garlicbears._quiz.domain.user.repository;

import garlicbears._quiz.domain.user.entity.Rating;
import garlicbears._quiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>, RatingQueryRepository {
    Optional<Rating> findByUser(User user);
}
