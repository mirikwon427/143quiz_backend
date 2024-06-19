package garlicbears._quiz.domain.game.repository;

import garlicbears._quiz.domain.game.entity.GameSession;
import garlicbears._quiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    Optional<GameSession> findByGameSessionIdAndUser(Long gameSessionId, User user);
}
