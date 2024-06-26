package garlicbears.quiz.domain.game.user.repository;

import java.util.List;

import garlicbears.quiz.domain.game.user.dto.GameStartQuestionDto;

public interface UserQuestionQueryRepository {
	//게임 시작 시 사용자에게 보여줄 문제 정보를 가져온다.
	List<GameStartQuestionDto> findGameQuestion(Long topicId, Long userId);
}
