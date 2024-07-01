package garlicbears.quiz.global.jwt.dto;

import jakarta.validation.constraints.NotEmpty;
/**
 * RefreshTokenDto 클래스.
 * JWT 리프레시 토큰을 전달하기 위한 DTO 클래스.
 */
public class RefreshTokenDto {
	@NotEmpty
	String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public RefreshTokenDto(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public RefreshTokenDto() {
	}
}
