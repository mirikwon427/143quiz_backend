package garlicbears._quiz.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user_answers")
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_answer_seq")
    private long userAnswerId;

    //  Topic
    //  Question
    //  User
    //  GameSession

    @Column(name = "user_answer_text", nullable = false)
    private String userAnswerText;

    @Column(name = "user_answer_status", nullable = false)
    private char userAnswerStatus;

    @Column(name = "hint_usage_count", nullable = false)
    @ColumnDefault("0")
    private int hintUsageCount;

    @Column(name = "user_answer_time_taken", nullable = false)
    @ColumnDefault("0")
    private int timeTaken;

    @Column(name = "user_answered_at", nullable = false)
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime userAnsweredAt;



}
