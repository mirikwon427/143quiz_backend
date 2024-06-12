package garlicbears._quiz.domain.game.repository;

import garlicbears._quiz.domain.game.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestionText(String questionText);
}
