package garlicbears.quiz.global.jwt.filter;

import java.io.IOException;

import java.util.logging.Logger;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import garlicbears.quiz.global.jwt.exception.JwtExceptionCode;
import garlicbears.quiz.global.jwt.token.JwtAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = "";
		try {
			token = getToken(request);
			if (StringUtils.hasText(token)) {
				getAuthentication(token);
			}
			filterChain.doFilter(request, response);
		} catch (AuthenticationException ex) {
			logger.warning("Authentication failed due to an AuthenticationException: " + ex.getMessage());
			request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN.getCode());
			logger.warning("Invalid Token // token : " + token);
			logger.warning("Set Request Exception Code : " + request.getAttribute("exception"));
			throw new BadCredentialsException("throw new invalid token exception", ex);
		} catch (ExpiredJwtException e) {
			request.setAttribute("exception", JwtExceptionCode.EXPIRED_TOKEN.getCode());
			logger.warning("EXPIRED Token // token : " + token);
			logger.warning("Set Request Exception Code : " + request.getAttribute("exception"));
			throw new BadCredentialsException("throw new expired token exception", e);
		} catch (UnsupportedJwtException | MalformedJwtException e) {
			request.setAttribute("exception", JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
			logger.warning("Unsupported Token // token : " + token);
			logger.warning("Set Request Exception Code : " + request.getAttribute("exception"));
			throw new BadCredentialsException("throw new unsupported token exception", e);
		} catch (Exception e) {
			logger.warning("====================================================");
			logger.warning("JwtFilter - doFilterInternal() 오류 발생");
			logger.warning("token : " + token);
			logger.warning("Exception Message : " + e.getMessage());
			logger.warning("Exception StackTrace : {");
			e.printStackTrace();
			logger.warning("}");
			logger.warning("====================================================");
			throw new BadCredentialsException("throw new exception", e);
		}
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7);
	}

	private void getAuthentication(String token) {
		JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
		Authentication authenticate = authenticationManager.authenticate(authenticationToken);

		if (authenticate != null) {
			logger.info("Authentication successful for token: " + token);
			//현재 요청에서 인증정보를 꺼낼 수 있도록 해준다.
			SecurityContextHolder.getContext().setAuthentication(authenticate);
			logger.info("SecurityContextHolder set with Authentication: " + SecurityContextHolder.getContext()
				.getAuthentication());
		} else {
			logger.warning("Authentication returned null for token: " + token);
		}
	}
}
