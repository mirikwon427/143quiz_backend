package garlicbears.quiz.domain.management.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.common.repository.UserRepository;
import garlicbears.quiz.domain.management.admin.dto.ResponseUserListDto;
import garlicbears.quiz.domain.management.common.dto.ResponseUserDto;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import jakarta.transaction.Transactional;

@Service
public class AdminUserService {
	private final UserRepository userRepository;

	public AdminUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public ResponseUserListDto getUserList(int pageNumber, int pageSize, String sort) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ResponseUserDto> page = userRepository.findUsers(pageNumber, pageSize, sort, pageable);

		return new ResponseUserListDto(sort, pageNumber, pageSize, page.getTotalPages(), page.getTotalElements(),
			page.getContent());
	}

	@Transactional
	public void delete(long userId) {
		User user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		user.setUserActive(Active.inactive);
		userRepository.save(user);
	}
}
