package garlicbears._quiz.domain.user.controller;

import garlicbears._quiz.domain.user.dto.ResponseUserDto;
import garlicbears._quiz.domain.user.dto.SignUpDto;
import garlicbears._quiz.domain.user.dto.UpdateUserDto;
import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.domain.user.service.RatingService;
import garlicbears._quiz.domain.user.service.UserService;
import garlicbears._quiz.global.config.auth.PrincipalDetails;
import garlicbears._quiz.global.dto.ResponseDto;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
@Tag(name="회원관리")
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;
    private final RatingService ratingService;

    @Autowired
    UserController(UserService userService, RatingService ratingService) {
        this.userService = userService;
        this.ratingService = ratingService;
    }

    @PostMapping("/checkEmail")
    @Operation(summary = "email 중복 확인", description = "입력된 이메일이 이미 가입된 유저인지 확인합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "email", schema = @Schema(type = "string", format = "json"))
                            }
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "Email Already Exist",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
    })
    public ResponseEntity<?> checkEmail(@Valid @RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        userService.checkDuplicatedEmail(email);
        return ResponseEntity.ok(ResponseDto.success());
    }

    @PostMapping("/checkNickname")
    @Operation(summary = "nickname 중복 확인", description = "입력된 닉네임이 이미 가입된 유저인지 확인합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "nickname", schema = @Schema(type = "string", format = "json"))
                            }
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "Nickname Already Exist",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
    })
    public ResponseEntity<?> checkNickname(@Valid @RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        userService.checkDuplicatedNickname(nickname);
        return ResponseEntity.ok(ResponseDto.success());
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "입력받은 정보를 바탕으로 회원을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))})
    })
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errorMessage.append(
                            error.getField()).append(": ").append(error.getDefaultMessage())
                    .append("."));
            logger.warning("errorMessage : " + errorMessage.toString());
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        userService.signUp(signUpDto);
        return ResponseEntity.ok(ResponseDto.success());
    }

    @GetMapping("/")
    @Operation(summary = "유저 정보(내 정보) 조회", description = "jwt token을 받아 유저 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)")
    })
    public ResponseEntity<?> searchUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 현재 인증된 사용자의 정보를 userDetails로부터 가져올 수 있습니다.
        User user = principalDetails.getUser();

        System.out.println(user);

        return ResponseEntity.ok(ResponseUserDto.fromUser(user));
    }

    @PatchMapping("/")
    @Operation(summary = "유저 정보(내 정보) 수정", description = "jwt token을 받아 유저 정보를 확인. resquest body로 전달받은 정보로 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request (Invalid Input)",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseUserDto.class))})
    })
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody UpdateUserDto updateUserDto) {
        // 현재 인증된 사용자의 정보를 userDetails로부터 가져올 수 있습니다.
        User user = principalDetails.getUser();

        // TODO: 사용자 정보 업데이트 로직 구현
        userService.update(user, updateUserDto);

        // 업데이트가 성공했을 때
        return ResponseEntity.ok(ResponseUserDto.fromUser(user));
    }

    @DeleteMapping("/")
    @Operation(summary = "계정 탈퇴", description = "jwt token을 받아 유저 정보를 확인. status 를 inactive로 변환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))})
    })
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();

        userService.delete(user);

        return ResponseEntity.ok(ResponseDto.success());
    }

    @PostMapping("/rating")
    @Operation(summary = "평점 주기", description = "jwt token을 받아 유저 정보를 확인. request body로 평점을 전달받아 저장합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schemaProperties = {
                                    @SchemaProperty(name = "ratingValue", schema = @Schema(type = "double", format = "json"))
                            }
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request (Invalid Input)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden (Invalid token)",
                    content = {@Content(schema = @Schema(implementation = ResponseDto.class))})
    })
    public ResponseEntity<?> rating(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody Map<String, Double> request) {
        User user = principalDetails.getUser();
        Double ratingValue = request.get("ratingValue");

        ratingService.saveRating(user, ratingValue);

        return ResponseEntity.ok(ResponseDto.success());
    }
}
