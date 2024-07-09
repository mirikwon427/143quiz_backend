package garlicbears.quiz.domain.game.user.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.game.common.entity.QReward;
import garlicbears.quiz.domain.game.common.entity.QTopic;
import garlicbears.quiz.domain.game.user.dto.TopicsListDto;
import jakarta.persistence.EntityManager;

@Repository
public class UserTopicQueryRepositoryImpl implements UserTopicQueryRepository {
	private final JPAQueryFactory queryFactory;

	@Autowired
	public UserTopicQueryRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<TopicsListDto> findUnacquaintedBadgeTopicsByUser(long userId) {
		QTopic topic = QTopic.topic;
		QReward reward = QReward.reward;

		return queryFactory
			.select(Projections.constructor(TopicsListDto.class,
				topic.topicId,
				topic.topicTitle,
				reward.rewardNumberHearts,
				topic.topicImage.accessUrl
			))
			.from(topic)
			.leftJoin(reward)
			.on(topic.topicId.eq(reward.topic.topicId)
				.and(reward.user.userId.eq(userId)))
			.where(topic.topicActive.eq(Active.active)
				.and((reward.topic.topicId.isNull()
					.or(reward.rewardBadgeStatus.eq(false)))))
			.groupBy(topic.topicId)
			.fetch();
	}

	@Override
	public List<TopicsListDto> findTopicsWithBadgeByUser(long userId) {
		QTopic topic = QTopic.topic;
		QReward reward = QReward.reward;

		return queryFactory
			.select(Projections.constructor(TopicsListDto.class,
				topic.topicId,
				topic.topicTitle,
				topic.topicImage.accessUrl
			))
			.from(topic)
			.join(reward)
			.on(topic.topicId.eq(reward.topic.topicId)
				.and(reward.user.userId.eq(userId)))
			.where(reward.rewardBadgeStatus.eq(true)
				.and(topic.topicActive.eq(Active.active)))
			.fetch();

	}

}
