package garlicbears.quiz.domain.management.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears.quiz.domain.management.admin.dto.ResponseAdminDto;

public interface AdminQueryRepository {
	Page<ResponseAdminDto> findAdmins(int page, int size, String sortBy, Pageable pageable);
}
