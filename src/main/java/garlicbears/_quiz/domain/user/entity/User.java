package garlicbears._quiz.domain.user.entity;

import garlicbears._quiz.domain.user.dto.SignUpDto;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static garlicbears._quiz.global.entity.Active.active;

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
    private Active userActive = active;

    @Builder
    public User(SignUpDto signUpDto, PasswordEncoder passwordEncoder) {
        this.userEmail = signUpDto.getEmail();
        this.userPassword = passwordEncoder.encode(signUpDto.getPassword());
        this.userNickname = signUpDto.getNickname();
        this.userBirthYear = signUpDto.getBirthYear();
        this.userAge = signUpDto.getAge();
        this.userGender = Gender.fromKoreanName(signUpDto.getGender());
        this.userLocation = Location.fromKoreanName(signUpDto.getLocation());
    }
}
