package garlicbears.quiz.domain.management.user.service;

import java.time.Year;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.Image;
import garlicbears.quiz.domain.common.entity.Role;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.repository.UserRepository;
import garlicbears.quiz.domain.management.common.repository.ImageRepository;
import garlicbears.quiz.domain.management.common.repository.RoleRepository;
import garlicbears.quiz.domain.management.common.service.ImageService;
import garlicbears.quiz.domain.management.user.dto.SignUpDto;
import garlicbears.quiz.domain.management.user.dto.UpdateUserDto;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	private final ImageRepository imageRepository;

	@Autowired
	UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
		RoleRepository roleRepository, ImageRepository imageRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.imageRepository = imageRepository;
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

		User.Gender gender = User.Gender.fromKoreanName(signUpDto.getGender());
		Image image = imageRepository.findByImageId((long)1);

		User user = new User.UserBuilder(signUpDto.getEmail(), passwordEncoder.encode(signUpDto.getPassword()),
			signUpDto.getNickname())
			.userBirthYear(signUpDto.getBirthYear())
			.userAge(Year.now().getValue() - signUpDto.getBirthYear())
			.userLocation(User.Location.fromKoreanName(signUpDto.getLocation()))
			.userGender(User.Gender.fromKoreanName(signUpDto.getGender()))
			.userRole(userRole)
			.userImage(image)
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
		Optional<User> user = userRepository.findByUserEmailAndUserActive(email, Active.active);
		if (user.isPresent()) {
			return user.get();
		} else {
			logger.error("AccessToken에 해당 유저의 정보가 없습니다. Email: {}", email);
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
	}

	@Transactional
	public void updateImage(User user, Image image) {
		user.setImage(image);
		userRepository.save(user);
	}
}
