package garlicbears._quiz.domain.user.service;

import garlicbears._quiz.domain.user.dto.UpdateUserDto;
import garlicbears._quiz.domain.user.entity.Gender;
import garlicbears._quiz.domain.user.entity.Location;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.domain.user.dto.SignUpDto;
import garlicbears._quiz.domain.user.repository.UserRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void checkDuplicatedEmail(String email) {
        Optional<User> user = userRepository.findByUserEmail(email);
        if(user.isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void checkDuplicatedNickname(String nickname) {
        Optional<User> user = userRepository.findByUserNickname(nickname);
        if(user.isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        User user = User.builder()
                .signUpDto(signUpDto)
                .passwordEncoder(passwordEncoder)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void update(User user, UpdateUserDto updateUserDto) {
        if (updateUserDto.getBirthYear() != null) {
            user.setUserBirthYear(updateUserDto.getBirthYear());
            user.setUserAge(Year.now().getValue() - updateUserDto.getBirthYear());
        }
        if (updateUserDto.getGender() != null) user.setUserGender(Gender.fromKoreanName(updateUserDto.getGender()));
        if (updateUserDto.getLocation() != null) user.setUserLocation(Location.fromKoreanName(updateUserDto.getLocation()));
        userRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        user.setUserActive(Active.inactive);
        userRepository.save(user);
    }
}
