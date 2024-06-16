package garlicbears._quiz.domain.game.entity;

import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "rewards")
public class Reward extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rewards_seq")
    private Long rewardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_seq")
    private Topic topic;
    @Column(name = "reward_number_hearts", nullable = false)
    private int rewardNumberHearts;
    @Column(name = "reward_badge_status")
    private boolean rewardBadgeStatus;

    public Reward(){}

    public Reward(Long rewardsId, User user, Topic topic, int rewardNumberHearts, boolean rewardBadgeStatus) {
        this.rewardId = rewardsId;
        this.user = user;
        this.topic = topic;
        this.rewardNumberHearts = rewardNumberHearts;
        this.rewardBadgeStatus = rewardBadgeStatus;
    }
    public Long getRewardId() {
        return rewardId;
    }

    public User getUser() {
        return user;
    }

    public Topic getTopic() {
        return topic;
    }

    public int getRewardNumberHearts() {
        return rewardNumberHearts;
    }

    public boolean getRewardBadgeStatus() {
        return rewardBadgeStatus;
    }
}

