package garlicbears.quiz.global.jwt.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
	private String token;
	private Object principal;
	private Object credentials;

	public JwtAuthenticationToken(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
	}

	public JwtAuthenticationToken(String token) {
		super(null);
		this.token = token;
		this.setAuthenticated(false);
	}

	public String getToken() {
		return this.token;
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
}
