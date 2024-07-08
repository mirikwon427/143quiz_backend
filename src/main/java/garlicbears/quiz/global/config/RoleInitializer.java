package garlicbears.quiz.global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import garlicbears.quiz.domain.common.entity.Role;
import garlicbears.quiz.domain.management.common.repository.RoleRepository;

@Configuration
public class RoleInitializer {

	@Bean
	public CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			if(roleRepository.count() == 0) { // role 테이블에 데이터가 없을 경우
				Role userRole = new Role();
				userRole.setRoleId(1L);
				userRole.setRoleName("ROLE_USER");

				Role adminRole = new Role();
				adminRole.setRoleId(2L);
				adminRole.setRoleName("ROLE_ADMIN");

				roleRepository.save(userRole);
				roleRepository.save(adminRole);
			}
		};
	}
}
