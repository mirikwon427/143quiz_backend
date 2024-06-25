package garlicbears.quiz.domain.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.management.admin.repository.AdminUserQueryRepository;
import garlicbears.quiz.domain.management.user.repository.UserQueryRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserQueryRepository, AdminUserQueryRepository {
	Optional<User> findByUserId(long userId);

	List<User> findByUserEmail(String email);

	List<User> findByUserNickname(String nickname);
}
