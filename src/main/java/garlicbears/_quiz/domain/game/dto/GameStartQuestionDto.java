package garlicbears._quiz.domain.game.dto;

public class GameStartQuestionDto {

    private Long questionId;

    private String questionText;

    private String answerText;

    public Long getQuestionId(){
        return questionId;
    }

    public String getQuestionText(){
        return questionText;
    }

    public String getAnswerText(){
        return answerText;
    }

    public GameStartQuestionDto(){}

    public GameStartQuestionDto(Long questionId, String questionText, String answerText) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answerText = answerText;
    }

}
