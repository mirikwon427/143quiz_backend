package garlicbears.quiz.domain.game.user.dto;

import garlicbears.quiz.domain.game.common.entity.UserAnswer;
import jakarta.validation.constraints.NotNull;

/**
 * 사용자가 문제를 푼 후 응답하는 DTO
 */

public class UserAnswerDto {
	@NotNull
	private Long questionId;

	@NotNull
	private String answerText;

	@NotNull
	private UserAnswer.AnswerStatus answerStatus;

	@NotNull
	private Integer hintUsageCount;

	@NotNull
	private Integer answerTimeTaken;

	@NotNull
	private String answerAt;

	public UserAnswerDto() {
	}

	public UserAnswerDto(Long questionId, String answerText, UserAnswer.AnswerStatus answerStatus, int hintUsageCount,
		int answerTimeTaken, String answerAt) {
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

	public UserAnswer.AnswerStatus getAnswerStatus() {
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
