package garlicbears._quiz.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import garlicbears._quiz.domain.game.entity.QReward;
import garlicbears._quiz.domain.user.dto.UserRankingDto;
import garlicbears._quiz.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class UserQueryRepositoryImpl implements UserQueryRepository{
    private final JPAQueryFactory queryFactory;

    public UserQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserRankingDto> findRankings(Pageable pageable) {
        QUser user = QUser.user;
        QReward reward = QReward.reward;

        List<UserRankingDto> content = queryFactory
                .select(Projections.constructor(UserRankingDto.class,
                        user.userId,
                        user.userNickname,
                        reward.rewardNumberHearts.sum().as("totalHearts"),
                        reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum().as("totalBadges")
//                        reward.rewardNumberHearts.sum().add(
//                                reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum()).as("totalScore")
                ))
                .from(reward)
                .join(reward.user, user)
                .groupBy(user.userId, user.userNickname)
                .orderBy(
//                        reward.rewardNumberHearts.sum().add(
//                                reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum()).desc(),
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

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<UserRankingDto> findRankingsByTopicId(long topicId, Pageable pageable) {
        QUser user = QUser.user;
        QReward reward = QReward.reward;

        List<UserRankingDto> content = queryFactory
                .select(Projections.constructor(UserRankingDto.class,
                        user.userId,
                        user.userNickname,
                        reward.rewardNumberHearts.sum().as("totalHearts"),
                        reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum().as("totalBadges")
//                        reward.rewardNumberHearts.sum().add(
//                                reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum()).as("totalScore")
                ))
                .from(reward)
                .join(reward.user, user)
                .where(reward.topic.topicId.eq(topicId))
                .groupBy(user.userId, user.userNickname)
                .orderBy(
//                        reward.rewardNumberHearts.sum().add(
//                                reward.rewardBadgeStatus.when(true).then(1).otherwise(0).sum()).desc(),
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

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
