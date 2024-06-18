package garlicbears._quiz.global.exception;

import org.springframework.http.HttpStatus;


public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_REQUIRED_FIELDS(HttpStatus.BAD_REQUEST, "필수 입력값이 누락되었습니다."),
    ILLEGAL_RATING_VALUE(HttpStatus.BAD_REQUEST, "별점 값은 0 과 5 사이여야 합니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    TOPIC_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 주제입니다."),
    QUESTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 문제입니다."),
    ALREADY_RATED(HttpStatus.CONFLICT, "이미 존재하는 문제입니다."),
    UNKNOWN_LOCATION(HttpStatus.NOT_FOUND, "주소가 틀렸습니다."),
    UNKNOWN_GENDER(HttpStatus.NOT_FOUND,"성별이 틀렸습니다."),
    UNKNOWN_TOPIC(HttpStatus.NOT_FOUND,"주제를 찾을 수 없습니다."),
    DELETED_TOPIC(HttpStatus.GONE,"삭제된 주제입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }
}
