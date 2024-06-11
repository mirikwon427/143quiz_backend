package garlicbears._quiz.domain.user.entity;

import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    male("남자"),
    female("여자"),
    other("기타");

    private final String koreanName;

    public static Gender fromKoreanName(String koreanName) {
        for(Gender gender: Gender.values()){
            if(gender.getKoreanName().equalsIgnoreCase(koreanName)) {
                return gender;
            }
        }
        throw new CustomException(ErrorCode.UNKNOWN_GENDER);
    }
}
