package garlicbears.quiz.global.jwt.exception;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		String exception = (String)request.getAttribute("exception");

		if (exception != null) {
			logger.error("Commence Get Exception : {}", exception);
			logger.error("entry point >> not found token");

			if (exception.equals(JwtExceptionCode.INVALID_TOKEN.getCode())) {
				logger.error("entry point >> invalid token");
				setResponse(response, JwtExceptionCode.INVALID_TOKEN);
			}
			//토큰 만료된 경우
			else if (exception.equals(JwtExceptionCode.EXPIRED_TOKEN.getCode())) {
				logger.error("entry point >> expired token");
				setResponse(response, JwtExceptionCode.EXPIRED_TOKEN);
			}
			//지원되지 않는 토큰인 경우
			else if (exception.equals(JwtExceptionCode.UNSUPPORTED_TOKEN.getCode())) {
				logger.error("entry point >> unsupported token");
				setResponse(response, JwtExceptionCode.UNSUPPORTED_TOKEN);
			} else if (exception.equals(JwtExceptionCode.NOT_FOUND_TOKEN.getCode())) {
				logger.error("entry point >> not found token");
				setResponse(response, JwtExceptionCode.NOT_FOUND_TOKEN);
			} else {
				setResponse(response, JwtExceptionCode.UNKNOWN_ERROR);
			}
		}
	}

	private void setResponse(HttpServletResponse response, JwtExceptionCode exceptionCode) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		HashMap<String, Object> errorInfo = new HashMap<>();
		errorInfo.put("message", exceptionCode.getMessage());
		errorInfo.put("code", exceptionCode.getCode());
		response.getWriter().print(errorInfo);
	}
}
