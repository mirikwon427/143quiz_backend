package garlicbears._quiz.domain.game.service;

import garlicbears._quiz.domain.game.dto.RequestUserAnswerDto;
import garlicbears._quiz.domain.game.dto.ResponseUserAnswerDto;
import garlicbears._quiz.domain.game.dto.UserAnswerDto;
import garlicbears._quiz.domain.game.entity.*;
import garlicbears._quiz.domain.game.repository.GameSessionRepository;
import garlicbears._quiz.domain.game.repository.QuestionRepository;
import garlicbears._quiz.domain.game.repository.RewardRepository;
import garlicbears._quiz.domain.game.repository.TopicRepository;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserAnswerService {

    private static final Logger logger = Logger.getLogger(UserAnswerService.class.getName());

    private final RewardRepository rewardRepository;

    private final GameSessionRepository gameSessionRepository;

    private final QuestionRepository questionRepository;

    private final TopicRepository topicRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UserAnswerService(RewardRepository rewardRepository, GameSessionRepository gameSessionRepository,
                      QuestionRepository questionRepository, TopicRepository topicRepository) {
        this.rewardRepository = rewardRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.questionRepository = questionRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public ResponseUserAnswerDto rewardUpdate(User user, RequestUserAnswerDto requestUserAnswerDto){
        Topic topic = Optional.of(topicRepository.findByTopicId(requestUserAnswerDto.getTopicId()))
                .orElseThrow(() -> new CustomException(ErrorCode.UNKNOWN_TOPIC));

        Optional<Reward> repositoryReward = rewardRepository.findByUserAndTopic(user, topic);

        Reward reward = repositoryReward.orElseGet(() ->
                new Reward(user, topic, requestUserAnswerDto.getHeartsCount()));

        long totalQuestions = questionRepository.countAllByTopicAndQuestionActive(topic, Active.active);
        int updatedHeartsCount = reward.getRewardNumberHearts() + requestUserAnswerDto.getHeartsCount();

        validateHeartsCount(updatedHeartsCount, totalQuestions);
        validateRewardBadgeStatus(reward.getRewardBadgeStatus());

        reward.setRewardNumberHearts(updatedHeartsCount);
        rewardRepository.save(reward);

        if (reward.getRewardNumberHearts() == totalQuestions) {
            reward.setRewardBadgeStatus(true);
            reward.setRewardBadgeCreatedAt(LocalDateTime.now());
            rewardRepository.save(reward);
            return new ResponseUserAnswerDto(totalQuestions, reward.getRewardNumberHearts(), true);
        }

        return new ResponseUserAnswerDto(totalQuestions, reward.getRewardNumberHearts(), false);
    }
    private void validateHeartsCount(int updatedHeartsCount, long totalQuestions) {
        if (updatedHeartsCount > totalQuestions) {
            logger.warning("updatedHeartsCount : " + updatedHeartsCount);
            logger.warning("totalQuestions : " + totalQuestions);
            throw new CustomException(ErrorCode.HEARTS_COUNT_EXCEEDS);
        }
    }

    private void validateRewardBadgeStatus(boolean rewardBadgeStatus) {
        if (rewardBadgeStatus) {
            throw new CustomException(ErrorCode.BADGE_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void userAnswerSave(User user, RequestUserAnswerDto requestUserAnswerDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        GameSession gameSession = gameSessionRepository.findByGameSessionIdAndUser(
                requestUserAnswerDto.getSessionId(), user)
                .orElseThrow(() -> new CustomException(ErrorCode.UNKNOWN_GAMESESSION));

        Topic topic = Optional.of(topicRepository.findByTopicId(requestUserAnswerDto.getTopicId()))
                .orElseThrow(() -> new CustomException(ErrorCode.UNKNOWN_TOPIC));

        List<UserAnswerDto> answers = requestUserAnswerDto.getAnswers();
        int length = answers.size();

        for(int i = 0; i < length; i++) {
            UserAnswerDto answerDto = answers.get(i);
            boolean isLastAnswer = (i == length - 1);

            if(isLastAnswer) {
                gameSession.setHeartsCount(requestUserAnswerDto.getHeartsCount());
                gameSession.setGameDropout(false);
                gameSession.setGameEndTime(LocalDateTime.parse(answerDto.getAnswerAt(), formatter));
            }

            Question question = questionRepository.findByQuestionId(answers.get(i).getQuestionId())
                    .orElseThrow(() -> new CustomException(ErrorCode.UNKNOWN_QUESTION));

            UserAnswer userAnswer = new UserAnswer.UserAnswerBuilder(user, gameSession, topic)
                    .question(question)
                    .userAnswerText(answerDto.getAnswerText())
                    .userAnswerStatus(answerDto.getAnswerStatus())
                    .hintUsageCount(answerDto.getHintUsageCount())
                    .timeTaken(answerDto.getAnswerTimeTaken())
                    .userAnsweredAt(LocalDateTime.parse(answerDto.getAnswerAt(), formatter))
                    .build();

            entityManager.persist(userAnswer);
        }
    }

    public void dropGameSession(long sessionId, User user) {
        GameSession gameSession = gameSessionRepository.findByGameSessionIdAndUser(sessionId, user)
                .orElseThrow(() -> new CustomException(ErrorCode.UNKNOWN_GAMESESSION));

        if(gameSession.getHeartsCount() == 0) {
            gameSession.setGameDropout(true);
            gameSession.setGameEndTime(LocalDateTime.now());
            gameSessionRepository.save(gameSession);
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

}
