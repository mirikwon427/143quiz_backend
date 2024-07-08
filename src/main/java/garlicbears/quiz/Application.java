package garlicbears.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition(servers = {
	@io.swagger.v3.oas.annotations.servers.Server(url = "https://garlicbears.com/api", description = "Cloud Server"),
	@io.swagger.v3.oas.annotations.servers.Server(url = "http://localhost:8080/api", description = "Local Server")
})
@SpringBootApplication
@EnableJpaAuditing
public class Application {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
