package garlicbears.quiz.global.jwt.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import garlicbears.quiz.global.jwt.token.JwtAuthenticationToken;
import garlicbears.quiz.global.jwt.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
	private static final Logger logger = Logger.getLogger(JwtAuthenticationProvider.class.getName());
	private final JwtTokenizer jwtTokenizer;
	private final UserDetailsService userDetailService;

	public JwtAuthenticationProvider(JwtTokenizer jwtTokenizer,
		UserDetailsService userDetailService) {
		this.jwtTokenizer = jwtTokenizer;
		this.userDetailService = userDetailService;
	}

	/**
	 * 인증을 진행하는 메소드
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {
		try {
			JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)authentication;
			Claims claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());
			String email = claims.getSubject();
			Long id = claims.get("id", Long.class);
			List<GrantedAuthority> authorities = getGrantedAuthorities(claims);

			UserDetails userDetails = userDetailService.loadUserByUsername(email);
			return new JwtAuthenticationToken(userDetails, null, authorities);

		} catch (MalformedJwtException mje) {
			logger.warning("error message " + mje.getMessage());
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}

	/**
	 * 권한을 가져오는 메소드
	 * @param claims
	 * @return
	 */
	private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
		List<String> roles = (List<String>)claims.get("roles");
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles) {
			authorities.add(() -> role);
		}
		return authorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
