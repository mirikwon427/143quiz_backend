package garlicbears.quiz.domain.management.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears._quiz.domain.admin.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	List<Admin> findByAdminEmail(String email);
}
