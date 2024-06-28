package garlicbears.quiz.domain.game.admin.dto;

public class GameStatDto {
	private long topicId;
	private String title;
	private long usageCount;
	private int completeCount;
	private int totalCorrectCount;

	public GameStatDto(long topicId, String title, long usageCount, int completeCount, int totalCorrectCount) {
		this.topicId = topicId;
		this.title = title;
		this.usageCount = usageCount;
		this.completeCount = completeCount;
		this.totalCorrectCount = totalCorrectCount;
	}

	public long getTopicId() {
		return topicId;
	}

	public String getTitle() {
		return title;
	}

	public long getUsageCount() {
		return usageCount;
	}

	public int getCompleteCount() {
		return completeCount;
	}

	public int getTotalCorrectCount() {
		return totalCorrectCount;
	}
}
