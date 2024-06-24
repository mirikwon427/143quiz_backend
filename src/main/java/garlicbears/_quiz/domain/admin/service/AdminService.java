package garlicbears._quiz.domain.admin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears._quiz.domain.admin.dto.AdminSignUpDto;
import garlicbears._quiz.domain.admin.entity.Admin;
import garlicbears._quiz.domain.admin.repository.AdminRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;

@Service
public class AdminService {
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void checkDuplicatedEmail(String email) {
		adminRepository.findByAdminEmail(email).forEach(admin -> {
			if (admin.getActive() == Active.active) {
				throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
			}
		});
	}

	@Transactional
	public void signUp(AdminSignUpDto signUpDto) {

		Admin admin = new Admin(signUpDto.getEmail(),
			passwordEncoder.encode(signUpDto.getPassword()));

		adminRepository.save(admin);
	}
}
