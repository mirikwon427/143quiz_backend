package garlicbears._quiz.domain.game.repository;

import garlicbears._quiz.domain.game.dto.ResponseTopicDto;
import garlicbears._quiz.domain.game.dto.TopicsListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicQueryRepository {
    Page<ResponseTopicDto> findTopics(int page, int size, String sortBy, Pageable pageable);

    public List<TopicsListDto> findUnacquaintedBadgeTopicsByUser(long userId);

    public List<TopicsListDto> findTopicsWithBadgeByUser(long userId);

}
