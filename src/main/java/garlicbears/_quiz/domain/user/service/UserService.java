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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Optional;

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
        Optional<User> user = userRepository.findByUserEmail(email);
        if(user.isPresent() && user.get().getUserActive() == Active.active){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public void checkDuplicatedNickname(String nickname) {
        Optional<User> user = userRepository.findByUserNickname(nickname);
        if(user.isPresent() && user.get().getUserActive() == Active.active) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        User user = User.builder()
                        .build(signUpDto,passwordEncoder);

        userRepository.save(user);
    }

    @Transactional
    public void update(User user, UpdateUserDto updateUserDto){
        if (updateUserDto.getBirthYear() != null) {
            int birthYear = updateUserDto.getBirthYear();
            if (birthYear > Year.now().getValue()) {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }
            user.setUserBirthYear(birthYear);
            user.setUserAge(Year.now().getValue() - birthYear);
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
