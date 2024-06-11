package garlicbears._quiz.domain.game.entity;

import garlicbears._quiz.domain.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
public class GameSessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_session_seq")
    private long gameSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_seq")
    Topics  topic;

    @Column(name = "game_started_at", nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime gameStartTime;

    @Column(name = "game_ended_at")
    private LocalDateTime gameEndTime;

    @Column(name = "game_dropout", nullable = false)
    @ColumnDefault("TRUE")
    private boolean gameDropout;

    @Column(name = "game_hearts_count", nullable = false)
    @ColumnDefault("0")
    private int heartsCount;

}
