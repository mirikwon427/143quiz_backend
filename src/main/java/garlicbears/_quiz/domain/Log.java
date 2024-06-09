package garlicbears._quiz.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_seq")
    private long logId;

    //  User

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

//    @Column(name = "user_agent", nullable = false)
//    private String userAgent;

    @Column(name = "log_url", nullable = false)
    private String url;

    @Column(name = "log_created_at", nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
