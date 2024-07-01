package garlicbears.quiz.domain.management.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.Admin;
import garlicbears.quiz.domain.common.repository.AdminRepository;
import garlicbears.quiz.domain.management.admin.dto.AdminSignUpDto;
import garlicbears.quiz.domain.management.admin.dto.ResponseAdminDto;
import garlicbears.quiz.domain.management.admin.dto.ResponseAdminListDto;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Service
public class AdminService {
	private final AdminRepository adminRepository;

	public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		this.adminRepository = adminRepository;
	}

	@Transactional
	public ResponseAdminListDto getAdminList(int pageNumber, int pageSize, String sort) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ResponseAdminDto> page = adminRepository.findAdmins(pageNumber, pageSize, sort, pageable);

		return new ResponseAdminListDto(sort, pageNumber, pageSize, page.getTotalPages(), page.getTotalElements(),
			page.getContent());
	}

	@Transactional
	public void delete(long adminId) {
		Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
		admin.setActive(Active.inactive);
		adminRepository.save(admin);
	}
}
