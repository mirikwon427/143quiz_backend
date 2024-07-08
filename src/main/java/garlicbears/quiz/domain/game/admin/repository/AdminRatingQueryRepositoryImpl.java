package garlicbears.quiz.domain.game.admin.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;


import jakarta.persistence.EntityManager;

@Repository
public class AdminRatingQueryRepositoryImpl implements AdminRatingQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public AdminRatingQueryRepositoryImpl(EntityManager em) {
		this.jpaQueryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Double getAverageRating() {
		QRating rating = QRating.rating;

		return jpaQueryFactory
			.select(
				rating.ratingValue.avg().as("averageRating")
			)
			.from(rating)
			.fetchOne();
	}
}
