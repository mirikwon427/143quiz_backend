package garlicbears._quiz.domain.user.entity;

import garlicbears._quiz.domain.game.entity.Reward;
import garlicbears._quiz.domain.user.dto.SignUpDto;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static garlicbears._quiz.global.entity.Active.active;

@Entity
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userId;

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

    @OneToMany(mappedBy = "user")
    private List<Reward> reward = new ArrayList<>();;

    User(){}
    User(String userEmail, String userPassword, String userNickname, int userBirthYear,
         int userAge, Gender userGender, Location userLocation) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userBirthYear = userBirthYear;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userLocation = userLocation;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public int getUserBirthYear() {
        return userBirthYear;
    }

    public int getUserAge() {
        return userAge;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public Active getUserActive() {
        return userActive;
    }

    public List<Reward> getRewards() {
        return reward;
    }

    public void setUserBirthYear(int userBirthYear){
        this.userBirthYear = userBirthYear;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }
    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public void setUserActive(Active userActive) {
        this.userActive = userActive;
    }
    public static class UserBuilder {
        public User build(SignUpDto signUpDto, PasswordEncoder passwordEncoder) {
            String userEmail = signUpDto.getEmail();
            String userPassword = passwordEncoder.encode(signUpDto.getPassword());
            String userNickname = signUpDto.getNickname();
            int userBirthYear = signUpDto.getBirthYear();
            int userAge = Year.now().getValue() - signUpDto.getBirthYear();
            Gender userGender = Gender.fromKoreanName(signUpDto.getGender());
            Location userLocation = Location.fromKoreanName(signUpDto.getLocation());
            return new User(userEmail, userPassword, userNickname, userBirthYear, userAge, userGender, userLocation);
        }
    }
    public static UserBuilder builder() {
        return new UserBuilder();
    }
}
