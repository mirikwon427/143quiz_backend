package garlicbears._quiz.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import garlicbears._quiz.domain.game.entity.QReward;
import garlicbears._quiz.domain.user.dto.UserRankingDto;
import garlicbears._quiz.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserQueryRepositoryImpl implements UserQueryRepository{
    private final JPAQueryFactory queryFactory;

    public UserQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserRankingDto> findRankings(Pageable pageable) {
        QUser user = QUser.user;
        QReward reward = QReward.reward;

        List<UserRankingDto> results = queryFactory
                .select(Projections.constructor(UserRankingDto.class,
                        user.userId,
                        user.userNickname,
                        reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum().as("totalBadges"),
                        reward.rewardNumberHearts.sum().as("totalHearts"),
                        Expressions.constant(0L)
                ))
                .from(reward)
                .join(reward.user, user)
                .groupBy(user.userId, user.userNickname)
                .orderBy(
                        reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum().desc(),
                        reward.rewardNumberHearts.sum().desc()
                        )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(reward.count())
                .from(reward)
                .groupBy(user.userId, user.userNickname)
                .fetchOne();

        // Calculate rank and add it to each UserRankingDTO
        List<UserRankingDto> rankedResults = IntStream.range(0, results.size())
                .mapToObj(i -> {
                    UserRankingDto dto = results.get(i);
                    return new UserRankingDto(
                            dto.getUserId(),
                            dto.getNickname(),
                            dto.getTotalHearts(),
                            dto.getTotalBadges(),
                            pageable.getOffset() + i + 1L // Calculate rank based on page offset
                    );
                })
                .collect(Collectors.toList());

        return new PageImpl<>(rankedResults, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<UserRankingDto> findRankingsByTopicId(long topicId, Pageable pageable) {
        QUser user = QUser.user;
        QReward reward = QReward.reward;

        List<UserRankingDto> results = queryFactory
                .select(Projections.constructor(UserRankingDto.class,
                        user.userId,
                        user.userNickname,
                        reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum().as("totalBadges"),
                        reward.rewardNumberHearts.sum().as("totalHearts"),
                        Expressions.constant(0L)
                ))
                .from(reward)
                .join(reward.user, user)
                .where(reward.topic.topicId.eq(topicId))
                .groupBy(user.userId, user.userNickname)
                .orderBy(reward.rewardNumberHearts.sum().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(reward.count())
                .from(reward)
                .groupBy(user.userId, user.userNickname)
                .fetchOne();

        // Calculate rank and add it to each UserRankingDTO
        List<UserRankingDto> rankedResults = IntStream.range(0, results.size())
                .mapToObj(i -> {
                    UserRankingDto dto = results.get(i);
                    return new UserRankingDto(
                            dto.getUserId(),
                            dto.getNickname(),
                            dto.getTotalHearts(),
                            dto.getTotalBadges(),
                            pageable.getOffset() + i + 1L // Calculate rank based on page offset
                    );
                })
                .collect(Collectors.toList());

        return new PageImpl<>(rankedResults, pageable, total == null ? 0 : total);
    }
}
