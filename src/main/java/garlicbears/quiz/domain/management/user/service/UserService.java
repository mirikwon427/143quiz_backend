package garlicbears.quiz.domain.management.user.service;

import java.time.Year;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.Role;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.repository.UserRepository;
import garlicbears.quiz.domain.management.common.repository.RoleRepository;
import garlicbears.quiz.domain.management.user.dto.SignUpDto;
import garlicbears.quiz.domain.management.user.dto.UpdateUserDto;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	@Autowired
	UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
		RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	public void checkDuplicatedEmail(String email) {
		userRepository.findByUserEmail(email).forEach(user -> {
			if (user.getUserActive() == Active.active) {
				throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
			}
		});
	}

	public void checkDuplicatedNickname(String nickname) {
		userRepository.findByUserNickname(nickname).forEach(user -> {
			if (user.getUserActive() == Active.active) {
				throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
			}
		});
	}

	@Transactional
	public void signUp(SignUpDto signUpDto) {
		Role userRole = roleRepository.findByRoleName("ROLE_USER")
			.orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND));

		User user = new User.UserBuilder(signUpDto.getEmail(), passwordEncoder.encode(signUpDto.getPassword()),
			signUpDto.getNickname())
			.userBirthYear(signUpDto.getBirthYear())
			.userAge(Year.now().getValue() - signUpDto.getBirthYear())
			.userLocation(User.Location.fromKoreanName(signUpDto.getLocation()))
			.userGender(User.Gender.fromKoreanName(signUpDto.getGender()))
			.userRole(userRole)
			.build();

		userRepository.save(user);
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

	@Transactional
	public void delete(User user) {
		user.setUserActive(Active.inactive);
		userRepository.save(user);
	}

	@Transactional
	public User findByEmail(String email) {
		return userRepository.findByUserEmailAndUserActive(email, Active.active)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

}
