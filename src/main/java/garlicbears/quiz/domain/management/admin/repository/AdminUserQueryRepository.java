package garlicbears.quiz.domain.management.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears.quiz.domain.management.common.dto.ResponseUserDto;

public interface AdminUserQueryRepository {
	Page<ResponseUserDto> findUsers(int page, int size, String sortBy, Pageable pageable);
}
