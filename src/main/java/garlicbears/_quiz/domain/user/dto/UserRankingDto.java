package garlicbears._quiz.domain.user.dto;

public class UserRankingDto {
    private long userId;
    private String nickname;
    private int badgeCount;
    private int heartCount;

    public UserRankingDto() {

    }

    public UserRankingDto(long userId, String nickname, int badgeCount, int heartCount) {
        this.userId = userId;
        this.nickname = nickname;
        this.badgeCount = badgeCount;
        this.heartCount = heartCount;
    }

    public long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public int getHeartCount() {
        return heartCount;
    }
}
