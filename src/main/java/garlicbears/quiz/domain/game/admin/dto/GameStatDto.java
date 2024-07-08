package garlicbears.quiz.domain.game.admin.dto;

public class GameStatDto {
	private long topicId;
	private String title;
	private int usageCount;
	private long completeCount;
	private int totalCorrectCount;
	private int totalPlayTime;
	private long questionCount;
	private double averageCompletePlayRate;
	private double averagePlayTime;
	private double averageCorrectionRate;

	public GameStatDto(long topicId, String title, int usageCount, int totalCorrectCount, long questionCount) {
		this.topicId = topicId;
		this.title = title;
		this.usageCount = usageCount;
		this.completeCount = 0;
		this.totalCorrectCount = totalCorrectCount;
		this.totalPlayTime = 0;
		this.questionCount = questionCount;
	}

	public void calculate()
	{
		if (usageCount == 0 || completeCount == 0)
		{
			averageCompletePlayRate = 0;
			averagePlayTime = 0;
			averageCorrectionRate = 0;
		}
		else
		{
			averageCompletePlayRate = (double) 100 * completeCount / usageCount;
			averagePlayTime = (double) totalPlayTime / completeCount;
			averageCorrectionRate = (double) 100 * totalCorrectCount / (completeCount * 10);
		}
	}

	public long getTopicId() {
		return topicId;
	}

	public String getTitle() {
		return title;
	}

	public int getUsageCount() {
		return usageCount;
	}

	public long getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(long completeCount) {
		this.completeCount = completeCount;
	}

	public int getTotalCorrectCount() {
		return totalCorrectCount;
	}

	public int getTotalPlayTime() {
		return totalPlayTime;
	}

	public void setTotalPlayTime(int totalPlayTime) {
		this.totalPlayTime = totalPlayTime;
	}

	public void setCompleteCountAndTotalPlayTime(TopicPlayTimeDto rhs)
	{
		this.completeCount = rhs.getCompleteCount();
		this.totalPlayTime = rhs.getTotalPlayTime();
	}

	public long getQuestionCount() {
		return questionCount;
	}

	public double getAverageCompletePlayRate() {
		return averageCompletePlayRate;
	}

	public double getAveragePlayTime() {
		return averagePlayTime;
	}

	public double getAverageCorrectionRate() {
		return averageCorrectionRate;
	}
}
