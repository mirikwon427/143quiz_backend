package garlicbears._quiz.domain.user.service;

import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears._quiz.domain.user.dto.ResponseUserDto;
import garlicbears._quiz.domain.user.dto.ResponseUserListDto;
import garlicbears._quiz.domain.user.dto.SignUpDto;
import garlicbears._quiz.domain.user.dto.UpdateUserDto;
import garlicbears._quiz.domain.user.entity.Gender;
import garlicbears._quiz.domain.user.entity.Location;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.domain.user.repository.UserRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
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

		User user = new User.UserBuilder(signUpDto.getEmail(), passwordEncoder.encode(signUpDto.getPassword()),
			signUpDto.getNickname())
			.userBirthYear(signUpDto.getBirthYear())
			.userAge(Year.now().getValue() - signUpDto.getBirthYear())
			.userLocation(Location.fromKoreanName(signUpDto.getLocation()))
			.userGender(Gender.fromKoreanName(signUpDto.getGender()))
			.build();

		userRepository.save(user);
	}

	public ResponseUserListDto getUserList(int pageNumber, int pageSize, String sort) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ResponseUserDto> page = userRepository.findUsers(pageNumber, pageSize, sort, pageable);

		return new ResponseUserListDto(sort, pageNumber, pageSize, page.getTotalPages(), page.getTotalElements(),
			page.getContent());
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
			user.setUserGender(Gender.fromKoreanName(updateUserDto.getGender()));
		}

		if (updateUserDto.getLocation() != null) {
			user.setUserLocation(Location.fromKoreanName(updateUserDto.getLocation()));
		}

		userRepository.save(user);
	}

	@Transactional
	public void delete(long userId) {
		User user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		user.setUserActive(Active.inactive);
		userRepository.save(user);
	}

	@Transactional
	public void delete(User user) {
		user.setUserActive(Active.inactive);
		userRepository.save(user);
	}
}
