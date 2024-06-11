package garlicbears._quiz.domain.user.entity;

import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Location {
    Seoul("서울"),
    Gyeonggi("경기"),
    Incheon("인천"),
    Chungcheong("충청"),
    Jeolla("전라"),
    Gyeongsang("경상"),
    Jeju("제주"),
    Overseas("해외");

    private final String koreanName;

    public static Location fromKoreanName(String koreanName) {
        for(Location location:Location.values()) {
            if(location.getKoreanName().equalsIgnoreCase(koreanName)){
                return location;
            }
        }
        throw new CustomException(ErrorCode.UNKNOWN_LOCATION);
    }
}
