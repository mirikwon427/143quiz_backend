package garlicbears._quiz.domain.game.dto;

import garlicbears._quiz.domain.game.entity.Question;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.global.entity.Active;

import java.time.LocalDateTime;

public class ResponseQuestionDto {
    private long topicId;
    private String topicText;
    private long questionId;
    private String questionText;
    private String questionAnswer;
    private Active questionActive;
    private LocalDateTime questionCreationDate;
    private LocalDateTime questionUpdateDate;

    public ResponseQuestionDto(long topicId, String topicText, long questionId, String questionText, String questionAnswer, Active questionActive, LocalDateTime questionCreationDate, LocalDateTime questionUpdateDate) {
        this.topicId = topicId;
        this.topicText = topicText;
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionAnswer = questionAnswer;
        this.questionActive = questionActive;
        this.questionCreationDate = questionCreationDate;
        this.questionUpdateDate = questionUpdateDate;
    }

    public long getTopicId() {
        return topicId;
    }

    public String getTopicText() {
        return topicText;
    }

    public long getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public Active getQuestionActive() {
        return questionActive;
    }

    public LocalDateTime getQuestionCreationDate() {
        return questionCreationDate;
    }

    public LocalDateTime getQuestionUpdateDate() {
        return questionUpdateDate;
    }
}
