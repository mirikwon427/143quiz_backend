package garlicbears.quiz.global.jwt.repository;

import java.util.Optional;

public interface RefreshTokenRepository {
	void save(String email, String token);

	Optional<String> findByEmail(String email);

	void delete(String token);
}
