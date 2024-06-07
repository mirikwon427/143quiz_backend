package garlicbears._quiz.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Topics extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_seq")
    private Long topicSeq;

    @Column(name = "topic_title")
    private String topicTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_active")
    private Active topicActive;

    @Column(name = "topic_usage_count")
    private int topicUsageCount;

    @OneToMany(mappedBy = "topics")
    private List<Questions> questions = new ArrayList<>();
}
