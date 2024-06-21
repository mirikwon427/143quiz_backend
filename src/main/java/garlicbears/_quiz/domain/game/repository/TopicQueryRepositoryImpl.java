package garlicbears._quiz.domain.game.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import garlicbears._quiz.domain.game.dto.ResponseTopicDto;
import garlicbears._quiz.domain.game.dto.TopicsListDto;
import garlicbears._quiz.domain.game.entity.QReward;
import garlicbears._quiz.domain.game.entity.QTopic;
import garlicbears._quiz.global.entity.Active;
import jakarta.persistence.EntityManager;

@Repository
public class TopicQueryRepositoryImpl implements TopicQueryRepository {
	private final JPAQueryFactory queryFactory;

	@Autowired
	public TopicQueryRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<ResponseTopicDto> findTopics(int page, int size, String sortBy, Pageable pageable) {
		QTopic topic = QTopic.topic;

		List<ResponseTopicDto> results = queryFactory
			.select(Projections.constructor(ResponseTopicDto.class,
				topic.topicId,
				topic.topicTitle,
				topic.topicActive,
				topic.createdAt,
				topic.updatedAt,
				topic.topicUsageCount
			))
			.from(topic)
			.orderBy(getOrderSpecifier(topic, sortBy))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(topic.count())
			.from(topic)
			.fetchOne();

		return new PageImpl<>(results, pageable, total == null ? 0 : total);
	}

	private OrderSpecifier<?> getOrderSpecifier(QTopic topic, String sortBy) {
		return switch (sortBy) {
			case "titleAsc" -> topic.topicTitle.asc();
			case "titleDesc" -> topic.topicTitle.desc();
			case "createdAtAsc" -> topic.createdAt.asc();
			case "createdAtDesc" -> topic.createdAt.desc();
			case "updatedAtAsc" -> topic.updatedAt.asc();
			case "updatedAtDesc" -> topic.updatedAt.desc();
			case "usageCountAsc" -> topic.topicUsageCount.asc();
			case "usageCountDesc" -> topic.topicUsageCount.desc();
			default -> topic.topicId.desc();
		};
	}

	@Override
	public List<TopicsListDto> findUnacquaintedBadgeTopicsByUser(long userId) {
		QTopic topic = QTopic.topic;
		QReward reward = QReward.reward;

		return queryFactory
			.select(Projections.constructor(TopicsListDto.class,
				topic.topicId,
				topic.topicTitle
			))
			.from(topic)
			.leftJoin(reward)
			.on(topic.topicId.eq(reward.topic.topicId)
				.and(reward.user.userId.eq(userId)))
			.where(topic.topicActive.eq(Active.active)
				.and((reward.topic.topicId.isNull()
					.or(reward.rewardBadgeStatus.eq(false)))))
			.fetch();

	}

	@Override
	public List<TopicsListDto> findTopicsWithBadgeByUser(long userId) {
		QTopic topic = QTopic.topic;
		QReward reward = QReward.reward;

		return queryFactory
			.select(Projections.constructor(TopicsListDto.class,
				topic.topicId,
				topic.topicTitle
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
