package garlicbears._quiz.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private Integer birthYear;
    private String gender;
    private String location;
}
