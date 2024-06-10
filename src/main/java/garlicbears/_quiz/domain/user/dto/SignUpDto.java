package garlicbears._quiz.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    private int id;

    @NotNull
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 200)
    private String password;

    @NotNull
    @Size(max = 100)
    private String nickname;

    @NotNull
    private int birthYear;

    @NotNull
    private int age;

    @NotNull
    private String gender;

    @NotNull
    private String location;
}
