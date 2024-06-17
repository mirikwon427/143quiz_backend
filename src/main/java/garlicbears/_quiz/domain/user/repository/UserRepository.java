package garlicbears._quiz.domain.user.repository;

import garlicbears._quiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserQueryRepository {
    Optional<User> findByUserEmail(String email);
    Optional<User> findByUserNickname(String nickname);
}
