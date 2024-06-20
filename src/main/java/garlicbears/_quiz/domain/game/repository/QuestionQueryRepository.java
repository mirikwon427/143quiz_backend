package garlicbears._quiz.domain.game.repository;

import java.util.List;

import garlicbears._quiz.domain.game.dto.GameStartQuestionDto;
import garlicbears._quiz.domain.game.dto.ResponseQuestionDto;
import garlicbears._quiz.domain.game.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionQueryRepository {
    Page<ResponseQuestionDto> findQuestions(int pageNumber, int pageSize, String sort, Pageable pageable);
    Page<ResponseQuestionDto> findQuestionsByTopicId(long topicId, int pageNumber, int pageSize, String sort, Pageable pageable);
    void deleteByTopic(Topic topic);
    List<GameStartQuestionDto> findGameQuestion(Long topicId, Long userId);
}
