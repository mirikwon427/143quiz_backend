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

	//주어진 주제와 활성 상태에 해당하는 모든 문제의 수를 반환한다.
	Long countAllByTopicAndQuestionActive(Topic topic, Active active);
}
