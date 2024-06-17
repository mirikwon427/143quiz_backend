package garlicbears._quiz.domain.game.entity;

import garlicbears._quiz.domain.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
public class Reward{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rewards_seq", nullable = false)
    private Long rewardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_seq")
    private Topic topic;

    @Column(name = "reward_number_hearts", nullable = false)
    @ColumnDefault("0")
    private int rewardNumberHearts;

    @Column(name = "reward_badge_status", nullable = false)
    private boolean rewardBadgeStatus;

    @Column(name = "reward_badge_created_at", nullable = false)
    private LocalDateTime rewardBadgeCreatedAt;

    Reward(){}

    Reward(User user, Topic topic, int rewardNumberHearts, boolean rewardBadgeStatus, LocalDateTime rewardBadgeCreatedAt) {
        this.user = user;
        this.topic = topic;
        this.rewardNumberHearts = rewardNumberHearts;
        this.rewardBadgeStatus = rewardBadgeStatus;
        this.rewardBadgeCreatedAt = rewardBadgeCreatedAt;
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

    public LocalDateTime getRewardBadgeCreatedAt() { return rewardBadgeCreatedAt; }
}

