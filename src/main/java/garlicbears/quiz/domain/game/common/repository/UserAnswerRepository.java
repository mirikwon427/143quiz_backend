package garlicbears.quiz.domain.game.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import garlicbears.quiz.domain.game.admin.repository.AdminUserAnswerQueryRepository;
import garlicbears.quiz.domain.game.common.entity.UserAnswer;
@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long>, AdminUserAnswerQueryRepository {
}
