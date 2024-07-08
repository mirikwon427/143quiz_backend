package garlicbears.quiz.domain.game.admin.dto;

/**
 * 평점 통계 정보를 담는 DTO
 */

public class RatingStatDto {
	private double averageRating;
	private long ratingCount;

	public RatingStatDto(double averageRating, long ratingCount) {
		this.averageRating = averageRating;
		this.ratingCount = ratingCount;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public long getRatingCount() {
		return ratingCount;
	}
}
