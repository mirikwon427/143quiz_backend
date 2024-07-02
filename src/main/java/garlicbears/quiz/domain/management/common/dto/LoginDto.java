package garlicbears.quiz.domain.management.common.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginDto {

	@NotEmpty
	private String email;

	@NotEmpty
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginDto() {
	}

	public LoginDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
