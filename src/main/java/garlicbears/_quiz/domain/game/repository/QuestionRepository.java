package garlicbears._quiz.domain.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears._quiz.domain.game.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionQueryRepository {
	List<Question> findByQuestionText(String questionText);
}
