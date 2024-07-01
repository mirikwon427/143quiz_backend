package garlicbears.quiz.global.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

/**
 * 테스트 환경에서 임베디드 Redis 서버를 시작하고 종료하는 역할.
 */
@Configuration
public class EmbeddedRedisConfig {

	private RedisServer redisServer;

	/**
	 * 애플리케이션이 시작될 때 임베디드 Redis 서버를 시작.
	 */
	@PostConstruct
	public void init() throws IOException {
		redisServer = new RedisServer(6379);
		redisServer.start();
	}

	/**
	 * 애플리케이션이 종료될 때 임베디드 Redis 서버를 종료.
	 */
	@PreDestroy
	public void destroy() throws IOException {
		redisServer.stop();
	}
}
