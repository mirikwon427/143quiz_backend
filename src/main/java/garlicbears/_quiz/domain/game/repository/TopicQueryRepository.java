package garlicbears._quiz.domain.game.repository;

import garlicbears._quiz.domain.game.dto.ResponseTopicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicQueryRepository {
    Page<ResponseTopicDto> findTopics(int page, int size, String sortBy, Pageable pageable);
}
