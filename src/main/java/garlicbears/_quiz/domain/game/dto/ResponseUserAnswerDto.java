package garlicbears._quiz.domain.game.dto;

public class ResponseUserAnswerDto {

    private int status;

    private Long totalQuestions;

    private int userHeartsCount;

    private boolean getBadge;

    public ResponseUserAnswerDto(){}

    public ResponseUserAnswerDto(int status, Long totalQuestions,
                                 int userHeartsCount, boolean getBadge) {
        this.status = status;
        this.totalQuestions = totalQuestions;
        this.userHeartsCount = userHeartsCount;
        this.getBadge = getBadge;
    }

    public int getStatus(){
        return status;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public int getUserHeartsCount() {
        return userHeartsCount;
    }

    public boolean isGetBadge() {
        return getBadge;
    }
}
