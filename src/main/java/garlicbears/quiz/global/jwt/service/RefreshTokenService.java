package garlicbears.quiz.global.jwt.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears.quiz.global.jwt.repository.RedisRefreshTokenRepository;

/**
 * 리프레시 토큰을 저장, 조회, 삭제하는 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
public class RefreshTokenService {
	private final RedisRefreshTokenRepository redisRefreshTokenRepository;

	public RefreshTokenService(RedisRefreshTokenRepository redisRefreshTokenRepository) {
		this.redisRefreshTokenRepository = redisRefreshTokenRepository;
	}

	/**
	 * 리프레시 토큰을 저장하는 메서드.
	 */
	@Transactional
	public void save(String email, String token, long ttl) {
		redisRefreshTokenRepository.save(email, token, ttl);
	}

	/**
	 * 리프레시 토큰을 삭제하는 메서드.
	 */
	@Transactional
	public void deleteRefreshToken(String refreshToken) {
		redisRefreshTokenRepository.delete(refreshToken);
	}

	/**
	 * 이메일로 리프레시 토큰을 조회하는 메서드.
	 */
	@Transactional(readOnly = true)
	public Optional<String> findRefreshToken(String email) {
		return redisRefreshTokenRepository.findByEmail(email);
	}
}
