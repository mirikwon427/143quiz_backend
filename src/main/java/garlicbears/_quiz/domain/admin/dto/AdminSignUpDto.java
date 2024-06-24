package garlicbears._quiz.domain.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdminSignUpDto {
	@NotNull
	@Size(max = 100)
	private String email;

	@NotNull
	@Size(max = 200)
	private String password;

	public @NotNull @Size(max = 100) String getEmail() {
		return email;
	}

	public @NotNull @Size(max = 200) String getPassword() {
		return password;
	}
}
