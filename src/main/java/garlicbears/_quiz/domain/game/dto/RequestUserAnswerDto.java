package garlicbears._quiz.domain.game.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class RequestUserAnswerDto {

	@NotNull
	private Long topicId;

	@NotNull
	private Long sessionId;

	@NotNull
	private Integer heartsCount;

	@NotNull
	private List<UserAnswerDto> answers;

	public RequestUserAnswerDto() {
	}

	public RequestUserAnswerDto(Long topicId, Long sessionId, int heartsCount, List<UserAnswerDto> answers) {
		this.topicId = topicId;
		this.sessionId = sessionId;
		this.heartsCount = heartsCount;
		this.answers = answers;
	}

	public Long getTopicId() {
		return topicId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public int getHeartsCount() {
		return heartsCount;
	}

	public List<UserAnswerDto> getAnswers() {
		return answers;
	}
}
