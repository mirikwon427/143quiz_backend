package garlicbears.quiz.global.config;

import java.util.Arrays;
import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

import garlicbears.quiz.global.jwt.exception.CustomAuthenticationEntryPoint;
import garlicbears.quiz.global.jwt.filter.JwtAuthenticationFilter;
import garlicbears.quiz.global.jwt.provider.JwtAuthenticationProvider;

@Configuration
public class SecurityConfig {
	private static final Logger logger = Logger.getLogger(SecurityConfig.class.getName());
	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final CorsFilter corsFilter;

	public SecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider, CorsFilter corsFilter) {
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
		this.corsFilter = corsFilter;
	}

	private static final String[] PERMIT_ALL_PATTERNS = new String[] {
		"/user/checkNickname", "/user/checkEmail", "/user/signup",
		"/user/login", "/admin/login",
		"/user/reissue",
		"/admin/reissue",
		"/v3/api-docs/**",
		"/swagger-resources",
		"/swagger-resources/**",
		"/configuration/ui",
		"/configuration/security",
		"/swagger-ui.html",
		"/swagger-ui/**",
		"/webjars/**",
		"/swagger",
		"/user/image"
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
		AuthenticationManager authenticationManager) throws Exception {
		if (authenticationManager == null) {
			logger.warning("AuthenticationManager is null");
		} else {
			logger.info("AuthenticationManager is not null");
		}

		httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.httpBasic(AbstractHttpConfigurer::disable)
			.addFilter(corsFilter)
			.authenticationProvider(jwtAuthenticationProvider)
			.authorizeHttpRequests(httpRequests -> httpRequests
				.requestMatchers(Arrays.stream(PERMIT_ALL_PATTERNS)
					.map(AntPathRequestMatcher::antMatcher)
					.toArray(AntPathRequestMatcher[]::new)
				).permitAll()
				.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
				.anyRequest().authenticated()
			)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			)
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager),
				UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		AuthenticationManager authenticationManager = configuration.getAuthenticationManager();
		if (authenticationManager == null) {
			logger.severe("AuthenticationManager is null in authenticationManagerBean method");
		} else {
			logger.info("AuthenticationManager is successfully injected in authenticationManagerBean method");
		}

		return authenticationManager;
	}
}