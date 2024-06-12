package garlicbears._quiz.domain.game.entity;

import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "questions")
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_seq")
    private Long questionSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_seq")
    private Topic topic;

    @Column(name = "question_text", nullable = false, length = 100)
    private String questionText;

    @Column(name = "question_answer_text", nullable = false, length = 100)
    private String questionAnswerText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_active", nullable = false)
    private Active questionActive = Active.active;

    @Builder
    public Question(Topic topic, String questionText, String questionAnswerText) {
        this.questionText = questionText;
        this.topic = topic;
        this.questionAnswerText = questionAnswerText;
    }

}
