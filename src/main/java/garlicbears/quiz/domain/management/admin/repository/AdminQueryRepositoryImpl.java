package garlicbears.quiz.domain.management.admin.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.QAdmin;
import garlicbears.quiz.domain.common.entity.QRole;
import garlicbears.quiz.domain.management.admin.dto.ResponseAdminDto;
import jakarta.persistence.EntityManager;
@Repository
public class AdminQueryRepositoryImpl implements AdminQueryRepository {
	private final JPAQueryFactory queryFactory;

	public AdminQueryRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
	}

	private OrderSpecifier<?> getOrderSpecifier(QAdmin admin, String sortBy) {
		return switch (sortBy) {
			case "createdAtAsc" -> admin.createdAt.asc();
			case "createdAtDesc" -> admin.createdAt.desc();
			default -> admin.adminId.asc();
		};
	}

	@Override
	public Page<ResponseAdminDto> findAdmins(int page, int size, String sortBy, Pageable pageable) {
		QAdmin admin = QAdmin.admin;
		QRole role = QRole.role;

		// Step 1: 관리자 정보 가져오기
		List<ResponseAdminDto> admins = queryFactory
			.select(Projections.constructor(ResponseAdminDto.class,
				admin.adminId,
				admin.adminEmail,
				admin.createdAt,
				admin.updatedAt))
			.from(admin)
			.where(admin.active.eq(Active.active))
			.orderBy(getOrderSpecifier(admin, sortBy))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// Step 2: 역할 목록 가져오기
		Map<Long, List<String>> rolesMap = queryFactory
			.from(admin)
			.leftJoin(admin.roles, role)
			.where(admin.adminId.in(admins.stream().map(ResponseAdminDto::getAdminId).collect(Collectors.toList())))
			.transform(GroupBy.groupBy(admin.adminId).as(GroupBy.list(role.roleName)));

		// Step 3: 결과를 매핑하여 DTO로 변환
		admins.forEach(adminDto -> adminDto.setAdminRole(rolesMap.get(adminDto.getAdminId())));

		Long total = queryFactory
			.select(admin.count())
			.from(admin)
			.fetchOne();

		return new PageImpl<>(admins, pageable, total == null ? 0 : total);
	}
}
