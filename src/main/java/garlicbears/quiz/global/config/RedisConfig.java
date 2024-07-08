package garlicbears.quiz.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 서버와의 연결을 설정하고 RedisTemplate을 정의.
 */
@Configuration
public class RedisConfig {

	/**
	 * RedisStandaloneConfiguration을 사용하여 Redis 서버에 연결.
	 */
	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Value("${spring.redis.password}")
	private String redisPassword;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
		config.setPassword(redisPassword);
		return new LettuceConnectionFactory(config);
	}

	/**
	 * Redis 서버와의 데이터 작업을 위해 사용.
	 */
	@Bean
	public RedisTemplate<String, String> redisTemplate(){
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		return redisTemplate;
	}
}
