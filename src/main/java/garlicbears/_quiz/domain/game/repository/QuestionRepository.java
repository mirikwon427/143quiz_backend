package garlicbears._quiz.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears._quiz.domain.game.entity.Question;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.global.entity.Active;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionQueryRepository {

	List<Question> findByQuestionText(String questionText);

	Optional<Question> findByQuestionId(Long questionId);

	Long countAllByTopicAndQuestionActive(Topic topic, Active active);
}
