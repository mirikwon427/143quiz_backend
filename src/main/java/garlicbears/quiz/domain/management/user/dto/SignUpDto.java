package garlicbears.quiz.domain.management.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 회원가입 DTO
 */

public class SignUpDto {
	@NotNull
	@Size(max = 100)
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
	private String email;

	@NotNull
	@Size(max = 200)
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
	private String password;

	@NotNull
	@Size(max = 100)
	@Pattern(regexp = "^(?!.*\\s)[a-zA-Z0-9가-힣\\uD83C-\\uDBFF\\uDC00-\\uDFFF]{3,20}$")
	private String nickname;

	@NotNull
	private int birthYear;

	@NotNull
	private String gender;

	@NotNull
	private String location;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getNickname() {
		return nickname;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public String getGender() {
		return gender;
	}

	public String getLocation() {
		return location;
	}
}
