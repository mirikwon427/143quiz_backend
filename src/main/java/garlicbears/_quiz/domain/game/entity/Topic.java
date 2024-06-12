package garlicbears._quiz.domain.game.entity;

import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "topics")
public class Topic extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_seq")
    private Long topicSeq;

    @Column(name = "topic_title", nullable = false, length = 100)
    private String topicTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_active", nullable = false)
    private Active topicActive = Active.active;

    @Column(name = "topic_usage_count", nullable = false)
    @ColumnDefault("0")
    private int topicUsageCount;

    @OneToMany(mappedBy = "topic")
    private List<Question> questions = new ArrayList<>();

    @Builder
    public Topic(String topicTitle) {
        this.topicTitle = topicTitle;
    }
}
