package garlicbears.quiz.domain.management.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.Admin;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.repository.AdminRepository;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Service
public class AdminService {
	private final AdminRepository adminRepository;

	@Autowired
	public AdminService(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	@Transactional
	public Admin findByEmail(String email) {
		return adminRepository.findByAdminEmailAndActive(email, Active.active)
			.orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
	}
}
