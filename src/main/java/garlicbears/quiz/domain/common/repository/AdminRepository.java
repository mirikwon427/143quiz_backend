package garlicbears.quiz.domain.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.Admin;
import garlicbears.quiz.domain.management.admin.repository.AdminQueryRepository;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminQueryRepository {
	List<Admin> findByAdminEmail(String email);

	Optional<Admin> findByAdminEmailAndActive(String email, Active active);
}
