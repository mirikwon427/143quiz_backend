package garlicbears.quiz.domain.game.admin.dto;

public class DashboardDto {
	private long totalVisitors;
	private long dailyActiveUsers;
	private long dailyGamePlays;
	private long totalUsers;
	private double averageRating;

	public DashboardDto(TotalVisitorCountDto totalVisitors, long dailyActiveUsers, long dailyGamePlays, long totalUsers,
		Double averageRating) {
		this.totalVisitors = totalVisitors.getTotalVisitors();
		this.dailyActiveUsers = dailyActiveUsers;
		this.dailyGamePlays = dailyGamePlays;
		this.totalUsers = totalUsers;
		this.averageRating = averageRating == null ? 0 : averageRating;
	}

	public long getTotalVisitors() {
		return totalVisitors;
	}

	public long getDailyActiveUsers() {
		return dailyActiveUsers;
	}

	public long getDailyGamePlays() {
		return dailyGamePlays;
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public double getAverageRating() {
		return averageRating;
	}
}
