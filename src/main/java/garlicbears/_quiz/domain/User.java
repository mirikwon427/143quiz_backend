package garlicbears._quiz.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_seq;
    private String user_email;
    private String user_password;
    private String user_nickname;
    private int user_birth_year;
    private int user_age;
    private Gender user_gender;
    private Location user_location;
    private Active user_active;

}

