package garlicbears.quiz.global.jwt.util;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * RedisClient 클래스.
 * Redis 서버와의 데이터 작업을 수행하는 유틸리티 클래스.
 */
@Component
public class RedisClient {
	private final RedisTemplate<String, String> redisTemplate;

	public RedisClient(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * Redis에 값을 저장하는 메서드.
	 *
	 * @param key   저장할 키
	 * @param Data  저장할 데이터
	 */
	public void setValues(String key, String Data) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, Data);
	}

	public void setValues(String key, String data, Duration duration) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, data, duration);
	}

	public String getValues(String key) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		return values.get(key);
	}

	public void deleteValues(String key) {
		redisTemplate.delete(key);
	}

}
