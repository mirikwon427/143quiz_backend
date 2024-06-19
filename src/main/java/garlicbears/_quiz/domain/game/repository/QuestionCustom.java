package garlicbears._quiz.domain.game.repository;

import garlicbears._quiz.domain.game.dto.GameStartQuestionDto;

import java.util.List;

public interface QuestionCustom {
    List<GameStartQuestionDto> findGameQuestion(Long topicId, Long userId);
}
