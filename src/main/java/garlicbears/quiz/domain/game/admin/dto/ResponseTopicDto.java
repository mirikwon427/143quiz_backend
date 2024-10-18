package garlicbears.quiz.domain.game.admin.dto;

import java.time.LocalDateTime;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.game.common.entity.Topic;

public class ResponseTopicDto {
	private long topicId;
	private String topicText;
	private LocalDateTime topicCreationDate;
	private LocalDateTime topicUpdateDate;
	private int topicUsageCount;
	private long topicQuestionCount;
	private String topicImage;

	public ResponseTopicDto(long topicId, String topicText, LocalDateTime topicCreationDate,
		LocalDateTime topicUpdateDate, int topicUsageCount, long topicQuestionCount, String topicImage) {
		this.topicId = topicId;
		this.topicText = topicText;
		this.topicCreationDate = topicCreationDate;
		this.topicUpdateDate = topicUpdateDate;
		this.topicUsageCount = topicUsageCount;
		this.topicQuestionCount = topicQuestionCount;
		this.topicImage = topicImage;
	}

	public long getTopicId() {
		return topicId;
	}

	public String getTopicText() {
		return topicText;
	}

	public LocalDateTime getTopicCreationDate() {
		return topicCreationDate;
	}

	public LocalDateTime getTopicUpdateDate() {
		return topicUpdateDate;
	}

	public int getTopicUsageCount() {
		return topicUsageCount;
	}

	public long getTopicQuestionCount() {
		return topicQuestionCount;
	}

	public String getTopicImage() {
		return topicImage;
	}
}
