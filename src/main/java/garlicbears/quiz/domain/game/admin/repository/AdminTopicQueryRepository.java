package garlicbears.quiz.domain.game.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears.quiz.domain.game.admin.dto.ResponseTopicDto;

public interface AdminTopicQueryRepository {
	Page<ResponseTopicDto> findTopics(int page, int size, String sortBy, Pageable pageable);
}
