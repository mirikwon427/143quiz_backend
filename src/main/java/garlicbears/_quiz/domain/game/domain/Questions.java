package garlicbears._quiz.domain.game.domain;

import garlicbears._quiz.global.domain.Active;
import garlicbears._quiz.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Questions extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_seq")
    private Long questionSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_seq")
    private Topics topics;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "question_answer_text")
    private String questionAnswerText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_active")
    private Active questionActive;

}
