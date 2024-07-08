package garlicbears.quiz.domain.management.user.dto;

import garlicbears.quiz.domain.common.entity.User;

public class UpdateUserDto {
	private Integer birthYear;
	private User.Gender gender;
	private User.Location location;

	public UpdateUserDto(Integer birthYear, User.Gender gender, User.Location location) {
		this.birthYear = birthYear;
		this.gender = gender;
		this.location = location;
	}

	public Integer getBirthYear() {
		return birthYear;
	}

	public User.Gender getGender() {
		return gender;
	}

	public User.Location getLocation() {
		return location;
	}
}
