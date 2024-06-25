package garlicbears.quiz.domain.management.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears.quiz.domain.common.dto.UserRankingDto;

public interface UserQueryRepository {
	Page<UserRankingDto> findRankings(Pageable pageable);

	Page<UserRankingDto> findRankingsByTopicId(long topicId, Pageable pageable);
}
