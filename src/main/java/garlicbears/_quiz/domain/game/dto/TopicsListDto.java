package garlicbears._quiz.domain.game.dto;

/**
 * 게임 주제 목록 DTO
 */

public class TopicsListDto {

	long topicId;

	String title;

	int heartsCount;

	public TopicsListDto() {
	}

	public TopicsListDto(long topicId, String title) {
		this.topicId = topicId;
		this.title = title;
	}

	public TopicsListDto(long topicId, String title, int heartsCount) {
		this.topicId = topicId;
		this.title = title;
		this.heartsCount = heartsCount;
	}

	public long getTopicId() {
		return topicId;
	}

	public String getTitle() {
		return title;
	}

	public int getHeartsCount() {
		return heartsCount;
	}
}
