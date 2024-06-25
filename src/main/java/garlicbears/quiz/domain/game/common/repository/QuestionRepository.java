package garlicbears.quiz.domain.game.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.game.admin.repository.AdminQuestionQueryRepository;
import garlicbears.quiz.domain.game.common.entity.Question;
import garlicbears.quiz.domain.game.common.entity.Topic;
import garlicbears.quiz.domain.game.user.repository.UserQuestionQueryRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, UserQuestionQueryRepository,
	AdminQuestionQueryRepository {

	List<Question> findByQuestionAnswerText(String questionAnswerText);

	Optional<Question> findByQuestionId(Long questionId);

	//주어진 주제와 활성 상태에 해당하는 모든 문제의 수를 반환한다.
	Long countAllByTopicAndQuestionActive(Topic topic, Active active);
}
