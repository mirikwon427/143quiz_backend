package garlicbears._quiz.domain.game.dto;

import jakarta.validation.constraints.NotNull;

public class UserAnswerDto {
	@NotNull
	private Long questionId;

	@NotNull
	private String answerText;

	@NotNull
	private char answerStatus;

	@NotNull
	private Integer hintUsageCount;

	@NotNull
	private Integer answerTimeTaken;

	@NotNull
	private String answerAt;

	public UserAnswerDto() {
	}

	public UserAnswerDto(Long questionId, String answerText, char answerStatus, int hintUsageCount, int answerTimeTaken,
		String answerAt) {
		this.questionId = questionId;
		this.answerText = answerText;
		this.answerStatus = answerStatus;
		this.hintUsageCount = hintUsageCount;
		this.answerTimeTaken = answerTimeTaken;
		this.answerAt = answerAt;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public String getAnswerText() {
		return answerText;
	}

	public char getAnswerStatus() {
		return answerStatus;
	}

	public int getHintUsageCount() {
		return hintUsageCount;
	}

	public int getAnswerTimeTaken() {
		return answerTimeTaken;
	}

	public String getAnswerAt() {
		return answerAt;
	}
}
