package garlicbears.quiz.global.jwt.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.Admin;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.repository.AdminRepository;
import garlicbears.quiz.domain.common.repository.UserRepository;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Component
public class UserDetailsServicelmpl implements UserDetailsService {
	private final UserRepository userRepository;
	private final AdminRepository adminRepository;

	public UserDetailsServicelmpl(UserRepository userRepository, AdminRepository adminRepository) {
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserEmailAndUserActive(email, Active.active);
		if (user.isPresent()) {
			return createUser(user.get());
		}

		Optional<Admin> admin = adminRepository.findByAdminEmailAndActive(email, Active.active);
		if (admin.isPresent()) {
			return createAdmin(admin.get());
		}

		throw new CustomException(ErrorCode.USER_NOT_FOUND);
	}

	private UserDetails createAdmin(Admin admin) {
		return new org.springframework.security.core.userdetails.User(admin.getAdminEmail(), admin.getPassword(),
			admin.getAuthorities());
	}

	private UserDetails createUser(User user) {
		return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPassword(),
			user.getAuthorities());
	}
}
