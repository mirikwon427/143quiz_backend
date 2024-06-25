package garlicbears.quiz.global.config.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.repository.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	public PrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername: " + username);
		Optional<User> userEntity = userRepository.findByUserEmail(username)
			.stream()
			.filter(user -> user.getUserActive() == Active.active)
			.findFirst();
		if (userEntity.isEmpty()) {
			throw new UsernameNotFoundException("not found userEmail : " + username);
		}

		return new PrincipalDetails(userEntity.get());
	}
}
