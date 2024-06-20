package garlicbears._quiz.global.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import garlicbears._quiz.global.dto.ResponseDto;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ResponseDto<String>> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		ResponseDto<String> response = new ResponseDto<>(errorCode.getStatus().value(),
			errorCode.toString());
		return new ResponseEntity<>(response, errorCode.getStatus());
	}
}
