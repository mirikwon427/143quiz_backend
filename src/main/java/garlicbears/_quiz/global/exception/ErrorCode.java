package garlicbears._quiz.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    UNKNOWN_LOCATION(HttpStatus.NOT_FOUND, "주소가 틀렸습니다."),
    UNKNOWN_GENDER(HttpStatus.NOT_FOUND,"성별이 틀렸습니다.");
    private final HttpStatus status;
    private final String message;
}
