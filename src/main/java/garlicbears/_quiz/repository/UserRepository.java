package garlicbears._quiz.repository;

import garlicbears._quiz.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUserEmail(String email);
}
