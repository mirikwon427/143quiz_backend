package garlicbears._quiz.domain.game.service;

import garlicbears._quiz.domain.game.entity.Question;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.domain.game.repository.QuestionRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 초성 19자
    private final String[] initialChs = {
            "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
            "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
            "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
            "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };

    @Transactional
    public void save(Topic topic, String questionText) {
        questionRepository.findByQuestionText(questionText).forEach(question -> {
            if (question.getQuestionActive() == Active.active){
                throw new CustomException(ErrorCode.QUESTION_ALREADY_EXISTS);
            }
        });

        StringBuilder questionAnswer = new StringBuilder();

        for (char c : questionText.toCharArray()) {
            if (c < '가' || c > '힣')
            {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }
            int initial = (((c - '가') / 28) / 21);
            questionAnswer.append(initialChs[initial]);
        }

        Question question = new Question(topic, questionAnswer.toString(), questionText);

        questionRepository.save(question);
    }
}
