package garlicbears._quiz.domain.user.domain;

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
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_birth_year")
    private int userBirthYear;

    @Column(name = "user_age")
    private int userAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    private Gender userGender;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_location")
    private Location userLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_active")
    private Active userActive;

}
