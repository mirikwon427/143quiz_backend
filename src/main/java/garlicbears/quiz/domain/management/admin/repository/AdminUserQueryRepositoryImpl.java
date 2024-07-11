package garlicbears.quiz.domain.management.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import garlicbears.quiz.domain.common.entity.Active;
import garlicbears.quiz.domain.common.entity.QUser;
import garlicbears.quiz.domain.management.common.dto.ResponseUserDto;
import jakarta.persistence.EntityManager;

@Repository
public class AdminUserQueryRepositoryImpl implements AdminUserQueryRepository {
	private final JPAQueryFactory queryFactory;

	public AdminUserQueryRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	private OrderSpecifier<?> getOrderSpecifier(QUser user, String sortBy) {
		return switch (sortBy) {
			case "idDesc" -> user.userId.desc();
			case "nicknameAsc" -> user.userNickname.asc();
			case "nicknameDesc" -> user.userNickname.desc();
			case "emailAsc" -> user.userEmail.asc();
			case "emailDesc" -> user.userEmail.desc();
			case "birthYearAsc" -> user.userBirthYear.asc();
			case "birthYearDesc" -> user.userBirthYear.desc();
			case "ageAsc" -> user.userAge.asc();
			case "ageDesc" -> user.userAge.desc();
			case "genderAsc" -> user.userGender.asc();
			case "genderDesc" -> user.userGender.desc();
			case "locationAsc" -> user.userLocation.asc();
			case "locationDesc" -> user.userLocation.desc();
			case "activeAsc" -> user.userActive.asc();
			case "activeDesc" -> user.userActive.desc();
			default -> user.userId.asc();
		};
	}

	@Override
	public Page<ResponseUserDto> findUsers(int page, int size, String sortBy, Pageable pageable) {
		QUser user = QUser.user;

		List<ResponseUserDto> results = queryFactory
			.select(Projections.constructor(ResponseUserDto.class,
				user.userId,
				user.userEmail,
				user.userNickname,
				user.userBirthYear,
				user.userAge,
				user.userGender,
				user.userLocation,
				user.image.accessUrl
			))
			.from(user)
			.where(user.userActive.eq(Active.active))
			.orderBy(getOrderSpecifier(user, sortBy))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(user.count())
			.from(user)
			.fetchOne();

		return new PageImpl<>(results, pageable, total == null ? 0 : total);
	}
}
