package garlicbears.quiz.domain.management.common.service;

import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.repository.UserRepository;
import garlicbears.quiz.domain.management.common.dto.UpdateUserDto;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public void update(User user, UpdateUserDto updateUserDto) {
		if (updateUserDto.getBirthYear() != null) {
			int birthYear = updateUserDto.getBirthYear();
			if (birthYear > Year.now().getValue()) {
				throw new CustomException(ErrorCode.INVALID_INPUT);
			}
			user.setUserBirthYear(birthYear);
			user.setUserAge(Year.now().getValue() - birthYear);
		}

		if (updateUserDto.getGender() != null) {
			user.setUserGender(User.Gender.fromKoreanName(updateUserDto.getGender()));
		}

		if (updateUserDto.getLocation() != null) {
			user.setUserLocation(User.Location.fromKoreanName(updateUserDto.getLocation()));
		}

		userRepository.save(user);
	}
}
