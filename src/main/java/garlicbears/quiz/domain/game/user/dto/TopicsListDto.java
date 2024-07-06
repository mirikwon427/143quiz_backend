package garlicbears.quiz.domain.game.user.dto;

/**
 * 게임 주제 목록 DTO
 */

public class TopicsListDto {

	long topicId;

	String title;

	int heartsCount;

	long totalQuestionsCount;

	String topicImage;

	public TopicsListDto() {
	}

	public TopicsListDto(long topicId, String title, String topicImage) {
		this.topicId = topicId;
		this.title = title;
		this.topicImage = topicImage;
	}

	public TopicsListDto(long topicId, String title, int heartsCount, String topicImage) {
		this.topicId = topicId;
		this.title = title;
		this.heartsCount = heartsCount;
		this.topicImage = topicImage;
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

	public long getTotalQuestionsCount() {
		return totalQuestionsCount;
	}

	public String getTopicImage() {
		return topicImage;
	}

	public void setTotalQuestionsCount(long totalQuestionsCount) {
		this.totalQuestionsCount = totalQuestionsCount;
	}
}
