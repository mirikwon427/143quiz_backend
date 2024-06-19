package garlicbears._quiz.domain.game.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import garlicbears._quiz.domain.game.dto.GameStartQuestionDto;
import garlicbears._quiz.domain.game.entity.QQuestion;
import garlicbears._quiz.domain.game.entity.QUserAnswer;
import garlicbears._quiz.global.entity.Active;
import jakarta.persistence.EntityManager;

import java.util.List;

public class QuestionRepositoryImpl implements QuestionCustom{
    private final JPAQueryFactory queryFactory;

    public QuestionRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public List<GameStartQuestionDto> findGameQuestion(Long topicId, Long userId){
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
