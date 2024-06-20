package garlicbears._quiz.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears._quiz.domain.user.dto.UserRankingDto;

public interface UserQueryRepository {
	Page<UserRankingDto> findRankings(Pageable pageable);

	Page<UserRankingDto> findRankingsByTopicId(long topicId, Pageable pageable);
}
