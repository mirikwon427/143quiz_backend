package garlicbears._quiz.domain.game.domain;

import garlicbears._quiz.global.domain.Active;
import garlicbears._quiz.global.domain.BaseTimeEntity;
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
public class Topics extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_seq")
    private Long topicSeq;

    @Column(name = "topic_title", nullable = false, length = 100)
    private String topicTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_active", nullable = false)
    @ColumnDefault("'active'")
    private Active topicActive;

    @Column(name = "topic_usage_count", nullable = false)
    @ColumnDefault("0")
    private int topicUsageCount;

    @OneToMany(mappedBy = "topics")
    private List<Questions> questions = new ArrayList<>();
}
