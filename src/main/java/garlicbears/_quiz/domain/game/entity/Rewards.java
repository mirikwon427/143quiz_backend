package garlicbears._quiz.domain.game.entity;

import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class Rewards extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rewards_seq")
    private Long rewardsId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_seq")
    private Topics topics;
    @Column(name = "reward_number_hearts", nullable = false)
    private int rewardNumberHearts;
    @Column(name = "reward_badge_status")
    private boolean rewardBadgeStatus;

    Rewards(){}

    Rewards(Long rewardsId, User user, Topics topics, int rewardNumberHearts, boolean rewardBadgeStatus) {
        this.rewardsId = rewardsId;
        this.user = user;
        this.topics = topics;
        this.rewardNumberHearts = rewardNumberHearts;
        this.rewardBadgeStatus = rewardBadgeStatus;
    }
    public Long getRewardsId() {
        return rewardsId;
    }

    public User getUser() {
        return user;
    }

    public Topics getTopics() {
        return topics;
    }

    public int getRewardNumberHearts() {
        return rewardNumberHearts;
    }

    public boolean getRewardBadgeStatus() {
        return rewardBadgeStatus;
    }
}

