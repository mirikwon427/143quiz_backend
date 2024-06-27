package garlicbears.quiz.global.config.jwt.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import garlicbears.quiz.global.config.jwt.dto.LoginInfoDto;
import garlicbears.quiz.global.config.jwt.token.JwtAuthenticationToken;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
	private static final Logger logger = Logger.getLogger(JwtAuthenticationProvider.class.getName());
	private static final String TOKEN_SUBJECT = "access token";
	private static final String REFRESH_TOKEN_SUBJECT = "refresh token";
	private final JwtTokenizer jwtTokenizer;

	public JwtAuthenticationProvider(JwtTokenizer jwtTokenizer) {
		this.jwtTokenizer = jwtTokenizer;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {
		try {
			JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)authentication;
			Claims claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());
			String email = claims.getSubject();
			Long id = claims.get("id", Long.class);
			List<GrantedAuthority> authorities = getGrantedAuthorities(claims);

			LoginInfoDto loginInfoDto = new LoginInfoDto();
			loginInfoDto.setId(id);
			loginInfoDto.setEmail(email);

			return new JwtAuthenticationToken(loginInfoDto, null, authorities);

		} catch(MalformedJwtException mje) {
			logger.warning("error message " + mje.getMessage());
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}

	private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
		List<String> role = (List<String>) claims.get("role");
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(String r : role) {
			authorities.add(() -> r);
		}
		return authorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
