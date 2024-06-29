package garlicbears.quiz.domain.game.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import garlicbears.quiz.domain.game.user.service.UserAnswerService;
import garlicbears.quiz.domain.game.user.dto.RequestUserAnswerDto;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.global.config.auth.PrincipalDetails;
import garlicbears.quiz.domain.common.dto.ResponseDto;
import garlicbears.quiz.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/game")
@Tag(name = "사용자 답변")
public class UserAnswerController implements SwaggerUserAnswerController {

	private final UserAnswerService userAnswerService;

	@Autowired
	UserAnswerController(UserAnswerService userAnswerService) {
		this.userAnswerService = userAnswerService;
	}

	@PostMapping("/answer")
	public ResponseEntity<?> userAnswerSave(@Valid @RequestBody RequestUserAnswerDto requestUserAnswerDto,
		BindingResult bindingResult,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		if (bindingResult.hasErrors()) {
			ErrorCode errorCode = ErrorCode.MISSING_REQUEST_BODY_VARIABLE;
			ResponseDto<String> response = new ResponseDto<>(errorCode.getStatus().value(), errorCode.toString());
			return new ResponseEntity<>(response, errorCode.getStatus());
		}

		User user = principalDetails.getUser();

		userAnswerService.userAnswerSave(user, requestUserAnswerDto);
		return ResponseEntity.ok(userAnswerService.rewardUpdate(user, requestUserAnswerDto));
	}

	@GetMapping("/answerDrop/{sessionId}")
	public ResponseEntity<?> userAnswerDrop(@PathVariable(value = "sessionId") long sessionId,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		User user = principalDetails.getUser();

		userAnswerService.dropGameSession(sessionId, user);

		return ResponseEntity.ok(ResponseDto.success());

	}
}
