package garlicbears._quiz.domain.user.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_seq")
    private long ratingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @Column(name = "rating_value", nullable = false)
    @ColumnDefault("5")
    private double ratingValue;

    @Column(name = "rating_at", nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime ratingDate;

    public Rating() {}

    public Rating(User user, double ratingValue) {
        this.user = user;
        this.ratingValue = ratingValue;
        this.ratingDate = LocalDateTime.now();
    }
}
