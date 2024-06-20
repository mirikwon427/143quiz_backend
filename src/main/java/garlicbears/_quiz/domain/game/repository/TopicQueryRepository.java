package garlicbears._quiz.domain.game.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears._quiz.domain.game.dto.ResponseTopicDto;

public interface TopicQueryRepository {
	Page<ResponseTopicDto> findTopics(int page, int size, String sortBy, Pageable pageable);
}
