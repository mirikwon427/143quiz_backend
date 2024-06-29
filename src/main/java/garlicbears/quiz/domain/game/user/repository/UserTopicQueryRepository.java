package garlicbears.quiz.domain.game.user.repository;

import java.util.List;

import garlicbears.quiz.domain.game.user.dto.TopicsListDto;

public interface UserTopicQueryRepository {
	//뱃지 미획득 주제 목록
	public List<TopicsListDto> findUnacquaintedBadgeTopicsByUser(long userId);

	//뱃지 획득 주제 목록
	public List<TopicsListDto> findTopicsWithBadgeByUser(long userId);

}
