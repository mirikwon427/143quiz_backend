package garlicbears._quiz.domain.game.entity;

import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Topics extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_seq")
    private Long topicsId;

    @Column(name = "topic_title", nullable = false, length = 100)
    private String topicTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_active", nullable = false)
    private Active topicActive;

    @Column(name = "topic_usage_count", nullable = false)
    private int topicUsageCount;

    @OneToMany(mappedBy = "topics")
    private List<Questions> questions;

    @OneToMany(mappedBy = "topics")
    private List<Rewards> rewards;

    protected Topics(){}

    public Topics(String topicTitle, Active topicActive, int topicUsageCount){
        this.topicTitle = topicTitle;
        this.topicActive = topicActive;
        this.topicUsageCount = topicUsageCount;
    }

    public Long getTopicsId(){
        return topicsId;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public Active getTopicActive() {
        return topicActive;
    }

    public int getTopicUsageCount() {
        return topicUsageCount;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public List<Rewards> getRewards() {
        return rewards;
    }
}
