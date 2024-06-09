package garlicbears._quiz.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ratingId;

    //  User

    @Column(name = "rating_value", nullable = false)
    @ColumnDefault("5")
    private double ratingValue;

    @Column(name = "rating_at", nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime ratingDate;

}
