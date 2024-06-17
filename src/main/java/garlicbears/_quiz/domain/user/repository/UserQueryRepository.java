package garlicbears._quiz.domain.user.repository;

import garlicbears._quiz.domain.user.dto.UserRankingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryRepository {
    Page<UserRankingDto> findRankings(Pageable pageable);
    Page<UserRankingDto> findRankingsByTopicId(long topicId, Pageable pageable);
}
