package garlicbears._quiz.domain.user.dto;

import garlicbears._quiz.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseUserDto {
    private long userId;
    private String email;
    private String nickname;
    private int birthYear;
    private int age;
    private String gender;
    private String location;

    public static ResponseUserDto fromUser(User user) {
        return ResponseUserDto.builder()
                .userId(user.getUserSeq())
                .email(user.getUserEmail())
                .nickname(user.getUserNickname())
                .birthYear(user.getUserBirthYear())
                .age(user.getUserAge())
                .gender(user.getUserGender().getKoreanName())
                .location(user.getUserLocation().getKoreanName())
                .build();
    }
}
