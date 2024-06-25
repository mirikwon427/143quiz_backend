package garlicbears.quiz.domain.game.user.dto;

public class UserRankingDto {
	private long userId;
	private String nickname;
	private int totalBadges;
	private int totalHearts;
	private long rank;

	public UserRankingDto() {
	}

	public UserRankingDto(long userId, String nickname, int totalBadges, int totalHearts, long rank) {
		this.userId = userId;
		this.nickname = nickname;
		this.totalBadges = totalBadges;
		this.totalHearts = totalHearts;
		this.rank = rank;
	}

	public long getRank() {
		return rank;
	}

	public long getUserId() {
		return userId;
	}

	public String getNickname() {
		return nickname;
	}

	public int getTotalBadges() {
		return totalBadges;
	}

	public int getTotalHearts() {
		return totalHearts;
	}
}
