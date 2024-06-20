package garlicbears._quiz.domain.game.service;

import garlicbears._quiz.domain.game.dto.ResponseQuestionDto;
import garlicbears._quiz.domain.game.dto.ResponseQuestionListDto;
import garlicbears._quiz.domain.game.entity.Question;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.domain.game.repository.QuestionRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    // 초성 19자
    private final String[] initialChs = {
            "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
            "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
            "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
            "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Transactional
    public ResponseQuestionListDto getQuestionList(int pageNumber, int pageSize, String sort) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ResponseQuestionDto> page = questionRepository.findQuestions(pageNumber, pageSize, sort, pageable);

        return new ResponseQuestionListDto(
                page.getContent(), sort, pageNumber, pageSize, page.getTotalPages(),
                page.getTotalElements());
    }

    @Transactional
    public ResponseQuestionListDto getQuestionListByTopic(Topic topic, int pageNumber, int pageSize, String sort) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ResponseQuestionDto> page = questionRepository.findQuestionsByTopicId(topic.getTopicId(), pageNumber, pageSize, sort, pageable);

        return new ResponseQuestionListDto(
                page.getContent(), sort, pageNumber, pageSize, page.getTotalPages(),
                page.getTotalElements());
    }

    private String createInitialAnswer(String questionText) {
        StringBuilder questionAnswer = new StringBuilder();

        for (char c : questionText.toCharArray()) {
            if (c < '가' || c > '힣')
            {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }
            int initial = (((c - '가') / 28) / 21);
            questionAnswer.append(initialChs[initial]);
        }

        return questionAnswer.toString();
    }

    @Transactional
    public Question save(Topic topic, String questionText) {
        questionRepository.findByQuestionText(questionText).forEach(question -> {
            if (question.getQuestionActive() == Active.active){
                throw new CustomException(ErrorCode.QUESTION_ALREADY_EXISTS);
            }
        });

        String questionAnswer = createInitialAnswer(questionText);

        Question question = new Question(topic, questionText, questionAnswer);

        return questionRepository.save(question);
    }

    @Transactional
    public void update(long questionId, String questionText) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        question.setQuestionText(questionText);
        question.setQuestionAnswerText(createInitialAnswer(questionText));

        questionRepository.save(question);
    }

    @Transactional
    public void delete(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        question.setQuestionActive(Active.inactive);

        questionRepository.save(question);
    }

    @Transactional
    public void deleteByTopic(Topic topic) {
        questionRepository.deleteByTopic(topic);
    }
}
