package garlicbears.quiz.domain.common.dto;

public class ResponseDto<T> {
	private int status;
	private String code;

	public ResponseDto(int status, String code) {
		this.status = status;
		this.code = code;
	}

	public ResponseDto(String code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public static <T> ResponseDto<T> success() {
		return new ResponseDto<>(200, "SUCCESS");
	}
}
