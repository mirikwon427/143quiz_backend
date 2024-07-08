package garlicbears.quiz.global.jwt.exception;

import org.springframework.http.HttpStatus;

public enum JwtExceptionCode {

	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN_ERROR", "UNKNOWN_ERROR"),
	NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "NOT_FOUND_TOKEN", "Headers에 토큰 형식의 값을 찾을 수 없음"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 토큰"),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "기간이 만료된 토큰"),
	UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "UNSUPPORTED_TOKEN", "지원하지 않는 토큰");

	private final HttpStatus status;
	private final String code;
	private final String message;

	JwtExceptionCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
