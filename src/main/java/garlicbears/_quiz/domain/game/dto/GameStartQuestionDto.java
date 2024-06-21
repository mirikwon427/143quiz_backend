package garlicbears._quiz.domain.game.dto;

public class GameStartQuestionDto {

    private long questionId;

    private String questionText;

    private String answerText;

    public long getQuestionId(){
        return questionId;
    }

    public String getQuestionText(){
        return questionText;
    }

    public String getAnswerText(){
        return answerText;
    }

    public GameStartQuestionDto(){}

    public GameStartQuestionDto(long questionId, String questionText, String answerText) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answerText = answerText;
    }

}
