package garlicbears.quiz.domain.game.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears.quiz.domain.game.admin.dto.ResponseQuestionDto;
import garlicbears.quiz.domain.game.common.entity.Topic;

public interface AdminQuestionQueryRepository {
	Page<ResponseQuestionDto> findQuestions(int pageNumber, int pageSize, String sort, Pageable pageable);

	Page<ResponseQuestionDto> findQuestionsByTopicId(long topicId, int pageNumber, int pageSize, String sort,
		Pageable pageable);

	void deleteByTopic(Topic topic);
}
