package garlicbears._quiz.domain.user.service;

import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.domain.user.dto.SignUpDto;
import garlicbears._quiz.domain.user.repository.UserRepository;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        }
    }

    public void checkDuplicatedNickname(String nickname) {
        Optional<User> user = userRepository.findByUserNickname(nickname);
        if(user.isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
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
}
