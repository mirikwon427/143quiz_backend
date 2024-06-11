package garlicbears._quiz.domain.user.controller;

import garlicbears._quiz.domain.user.dto.ResponseUserDto;
import garlicbears._quiz.domain.user.dto.SignUpDto;
import garlicbears._quiz.domain.user.dto.UpdateUserDto;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.domain.user.service.UserService;
import garlicbears._quiz.global.config.auth.PrincipalDetails;
import garlicbears._quiz.global.dto.ResponseDto;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(@Valid @RequestBody Map<String, String> request) {
        String email = request.get("email");
        if(email == null || email.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        userService.checkDuplicatedEmail(email);
        return ResponseEntity.ok(ResponseDto.success());
    }

    @PostMapping("/checkNickname")
    public ResponseEntity<?> checkNickname(@Valid @RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        if(nickname == null || nickname.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        userService.checkDuplicatedNickname(nickname);
        return ResponseEntity.ok(ResponseDto.success());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errorMessage.append(
                    error.getField()).append(": ").append(error.getDefaultMessage())
                    .append("."));
            log.error("{}", errorMessage);
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        userService.signUp(signUpDto);
        return ResponseEntity.ok(ResponseDto.success());
    }

    @GetMapping("/")
    public ResponseEntity<?> searchUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 현재 인증된 사용자의 정보를 userDetails로부터 가져올 수 있습니다.
        User user = principalDetails.getUser();

        System.out.println(user);

        return ResponseEntity.ok(ResponseUserDto.fromUser(user));
    }

    @PatchMapping("/")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody UpdateUserDto updateUserDto) {
        // 현재 인증된 사용자의 정보를 userDetails로부터 가져올 수 있습니다.
        User user = principalDetails.getUser();

        // TODO: 사용자 정보 업데이트 로직 구현
        userService.update(user, updateUserDto);

        // 업데이트가 성공했을 때
        return ResponseEntity.ok(ResponseUserDto.fromUser(user));
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();

        userService.delete(user);

        return ResponseEntity.ok(ResponseDto.success());
    }

}
