package garlicbears._quiz.domain.user.domain;

import garlicbears._quiz.global.domain.Active;
import garlicbears._quiz.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import static garlicbears._quiz.global.domain.Active.active;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_email", nullable = false, unique = true, length = 200)
    private String userEmail;

    @Column(name = "user_password", nullable = false, length = 300)
    private String userPassword;

    @Column(name = "user_nickname", nullable = false, length = 100)
    private String userNickname;

    @Column(name = "user_birth_year", nullable = false)
    private int userBirthYear;

    @Column(name = "user_age", nullable = false)
    private int userAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private Gender userGender;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_location", nullable = false)
    private Location userLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_active", nullable = false)
    @ColumnDefault("'active'")
    private Active userActive;

}
