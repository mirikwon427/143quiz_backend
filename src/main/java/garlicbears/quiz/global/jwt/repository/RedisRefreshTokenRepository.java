package garlicbears.quiz.global.jwt.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import garlicbears.quiz.global.jwt.util.RedisClient;

/**
 * Redis를 이용하여 리프레시 토큰을 저장, 조회, 삭제하는 기능 구현
 */
@Repository
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

	private final RedisClient redisClient;

	public RedisRefreshTokenRepository(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

	/**
	 * 리프레시 토큰을 저장하는 메서드.
	 *
	 * @param email 저장할 이메일
	 * @param token 저장할 토큰
	 * @param ttl 토큰의 유효 시간 (밀리초 단위)
	 */
	@Override
	public void save(String email, String token, long ttl) {
		redisClient.setValues(email, token, Duration.ofMillis(ttl));
	}

	/**
	 * 이메일로 리프레시 토큰을 조회하는 메서드.
	 *
	 * @param email 조회할 이메일
	 * @return 해당 이메일에 저장된 토큰을 Optional로 반환
	 */
	@Override
	public Optional<String> findByEmail(String email) {
		return Optional.ofNullable(redisClient.getValues(email));
	}

	/**
	 * 이메일로 리프레시 토큰을 삭제하는 메서드.
	 *
	 * @param email 삭제할 이메일
	 */
	@Override
	public void delete(String email) {
		redisClient.deleteValues(email);
	}

}
