package garlicbears.quiz.domain.management.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears.quiz.domain.common.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleName(String roleName);
}
