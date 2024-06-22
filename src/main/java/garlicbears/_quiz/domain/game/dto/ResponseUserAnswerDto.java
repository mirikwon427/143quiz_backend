package garlicbears._quiz.domain.game.dto;

public class ResponseUserAnswerDto {

	private long totalQuestions;

	private int userHeartsCount;

	private boolean getBadge;

	public ResponseUserAnswerDto() {
	}

	public ResponseUserAnswerDto(long totalQuestions,
		int userHeartsCount, boolean getBadge) {
		this.totalQuestions = totalQuestions;
		this.userHeartsCount = userHeartsCount;
		this.getBadge = getBadge;
	}

	public long getTotalQuestions() {
		return totalQuestions;
	}

	public int getUserHeartsCount() {
		return userHeartsCount;
	}

	public boolean isGetBadge() {
		return getBadge;
	}
}
