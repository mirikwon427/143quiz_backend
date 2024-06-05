package garlicbears._quiz.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_session_seq")
    private Long gameSessionId;

    // Topic
    // User

    @Column(name = "game_started_at", nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime gameStartTime;

    @Column(name = "game_ended_at")
    private LocalDateTime gameEndTime;

    @Column(name = "game_dropout", nullable = false)
    @ColumnDefault("TRUE")
    private Boolean gameDropout;

    @Column(name = "game_hearts_count", nullable = false)
    @ColumnDefault("0")
    private Integer heartsCount;

}
