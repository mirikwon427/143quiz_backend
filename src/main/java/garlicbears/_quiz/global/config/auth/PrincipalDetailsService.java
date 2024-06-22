package garlicbears._quiz.global.config.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import garlicbears._quiz.domain.user.entity.User;
import garlicbears._quiz.domain.user.repository.UserRepository;
import garlicbears._quiz.global.entity.Active;

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
