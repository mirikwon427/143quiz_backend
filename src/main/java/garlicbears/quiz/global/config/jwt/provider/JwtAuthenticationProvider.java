package garlicbears.quiz.global.config.jwt.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import garlicbears.quiz.global.config.jwt.JwtAuthenticationFilter;
import garlicbears.quiz.global.config.jwt.JwtTokenizer;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
	private final JwtTokenizer jwtTokenizer;

	public JwtAuthenticationProvider(JwtTokenizer jwtTokenizer) {
		this.jwtTokenizer = jwtTokenizer;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}
}
