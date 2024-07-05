package garlicbears.quiz.domain.management.common.service;

import org.springframework.stereotype.Service;

import garlicbears.quiz.domain.common.entity.Role;
import garlicbears.quiz.domain.management.common.repository.RoleRepository;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Service
public class RoleService {
	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role findByRoleName(String roleName) {
		return roleRepository.findByRoleName(roleName)
			.orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND));
	}
}
