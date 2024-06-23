package garlicbears._quiz.domain.game.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears._quiz.domain.game.dto.ResponseTopicDto;
import garlicbears._quiz.domain.game.dto.TopicsListDto;

public interface TopicQueryRepository {
	Page<ResponseTopicDto> findTopics(int page, int size, String sortBy, Pageable pageable);

	//뱃지 미획득 주제 목록
	public List<TopicsListDto> findUnacquaintedBadgeTopicsByUser(long userId);

	//뱃지 획득 주제 목록
	public List<TopicsListDto> findTopicsWithBadgeByUser(long userId);

}
