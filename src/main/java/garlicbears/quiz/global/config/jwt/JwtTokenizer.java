package garlicbears.quiz.global.config.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenizer {

	private static final Logger logger = Logger.getLogger(JwtTokenizer.class.getName());
	private final byte[] accessSecret;
	private final byte[] refreshSecret;

	public JwtTokenizer(@Value("${secretKey}")String accessSecret, @Value("${refreshKey}")String refreshSecret) {
		this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
		this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
	}

	public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30 minutes
	public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days

	/**
	 * AccessToken 생성
	 */
	public String createAccessToken(Authentication authentication) {
		return createToken(authentication, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret);
	}

	/**
	 * RefreshToken 생성
	 */
	public String createRefreshToken(Authentication authentication) {
		return createToken(authentication, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret);
	}

	private String createToken(Authentication authentication, Long expire, byte[] secretKey) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		Claims claims = Jwts.claims()
			.subject(authentication.getName())
			.add("auth", authorities)
			.build();

		Date date = new Date();

		return Jwts.builder()
			.claims(claims)
			.issuedAt(date)
			.signWith(getSigningKey(secretKey))
			.expiration(new Date(date.getTime() + expire))
			.compact();
	}

	/**
	 * AccessToken 검증
	 */
	public Claims parseAccessToken(String accessToken) {
		return verify(accessToken, accessSecret);
	}

	/**
	 * RefreshToken 검증
	 */
	public Claims parseRefreshToken(String refreshToken) {
		return verify(refreshToken, refreshSecret);
	}

	public Claims verify(String token, byte[] secretKey) {
		try{
			return Jwts.parser()
				.verifyWith(getSigningKey(secretKey)) //이 키는 토큰의 서명이 해당 키로 생성되었는지 확인
				.build()
				.parseSignedClaims(token) //토큰의 내용을 확인, 토큰의 서명이 설정된 키로부터 유효한지 검증
				.getPayload();
		} catch(JwtException e) {
			logger.warning(e.getMessage());
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}

	/**
	 * @return Key 형식 시크릿 키
	 */
	public static SecretKey getSigningKey(byte[] secretKey){
		return Keys.hmacShaKeyFor(secretKey);
	}
}
