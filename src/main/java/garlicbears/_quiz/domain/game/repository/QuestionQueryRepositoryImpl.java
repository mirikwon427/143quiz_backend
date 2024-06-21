package garlicbears._quiz.domain.game.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import garlicbears._quiz.domain.game.dto.GameStartQuestionDto;
import garlicbears._quiz.domain.game.dto.ResponseQuestionDto;
import garlicbears._quiz.domain.game.entity.QQuestion;
import garlicbears._quiz.domain.game.entity.QUserAnswer;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.global.entity.Active;
import jakarta.persistence.EntityManager;

@Repository
public class QuestionQueryRepositoryImpl implements QuestionQueryRepository {
	private final JPAQueryFactory queryFactory;

	public QuestionQueryRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	private OrderSpecifier<?> getOrderSpecifier(QQuestion question, String sort) {
		return switch (sort) {
			case "questionTextAsc" -> question.questionText.asc();
			case "questionTextDesc" -> question.questionText.desc();
			case "createdAtAsc" -> question.createdAt.asc();
			case "createdAtDesc" -> question.createdAt.desc();
			case "updatedAtAsc" -> question.updatedAt.asc();
			case "updatedAtDesc" -> question.updatedAt.desc();
			default -> question.questionId.asc();
		};
	}

	private JPAQuery<ResponseQuestionDto> createBaseQuery(QQuestion question, String sort) {
		// 문제 조회를 위한 기본 쿼리 생성
		return queryFactory.select(
			Projections.constructor(ResponseQuestionDto.class, question.topic.topicId, question.topic.topicTitle,
				question.questionId, question.questionText, question.questionAnswerText, question.questionActive,
				question.createdAt, question.updatedAt)).from(question).orderBy(getOrderSpecifier(question, sort));
	}

	@Override
	public Page<ResponseQuestionDto> findQuestions(int pageNumber, int pageSize, String sort, Pageable pageable) {
		QQuestion question = QQuestion.question;
		JPAQuery<ResponseQuestionDto> baseQuery = createBaseQuery(question, sort);

		List<ResponseQuestionDto> results = baseQuery.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory.select(question.count()).from(question).fetchOne();

		return new PageImpl<>(results, pageable, total == null ? 0 : total);
	}

	@Override
	public Page<ResponseQuestionDto> findQuestionsByTopicId(long topicId, int pageNumber, int pageSize, String sort,
		Pageable pageable) {
		QQuestion question = QQuestion.question;
		// 주제별 문제 조회를 위한 쿼리 생성
		JPAQuery<ResponseQuestionDto> baseQuery = createBaseQuery(question, sort).where(
			question.topic.topicId.eq(topicId));

		List<ResponseQuestionDto> results = baseQuery.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory.select(question.count())
			.from(question)
			.where(question.topic.topicId.eq(topicId))
			.fetchOne();

		return new PageImpl<>(results, pageable, total == null ? 0 : total);
	}

	@Override
	public void deleteByTopic(Topic topic) {
		QQuestion question = QQuestion.question;
		queryFactory.update(question)
			.set(question.questionActive, Active.inactive)
			.where(question.topic.eq(topic))
			.execute();
	}

	@Override
	public List<GameStartQuestionDto> findGameQuestion(Long topicId, Long userId) {
		QQuestion question = QQuestion.question;
		QUserAnswer userAnswer = QUserAnswer.userAnswer;

		QUserAnswer userAnswer2 = new QUserAnswer("userAnswer2");
		BooleanExpression notExistsSubQuery = Expressions.asBoolean(
			JPAExpressions.selectOne()
				.from(userAnswer2)
				.where(userAnswer2.user.userId.eq(userId)
					.and(userAnswer2.question.questionId.eq(
						question.questionId))
					.and(userAnswer2.topic.topicId.eq(question.topic.topicId))
					.and(userAnswer2.userAnswerStatus.eq('Y')))
				.notExists()
		);

		BooleanExpression conditions = question.topic.topicId.eq(topicId)
			.and(question.questionActive.eq(Active.active))
			.and(userAnswer.question.questionId.isNull()
				.or(userAnswer.userAnswerStatus.eq('N').or(
					userAnswer.userAnswerStatus.eq('P')
				)))
			.and(notExistsSubQuery);

		int limit = 10;

		return queryFactory
			.select(Projections.constructor(GameStartQuestionDto.class,
				question.questionId,
				question.questionText,
				question.questionAnswerText
			))
			.from(question)
			.leftJoin(userAnswer)
			.on(question.questionId.eq(userAnswer.question.questionId)
				.and(question.topic.topicId.eq(userAnswer.topic.topicId))
				.and(userAnswer.user.userId.eq(userId)))
			.where(conditions)
			.distinct()
			.limit(limit)
			.fetch();
	}

}
