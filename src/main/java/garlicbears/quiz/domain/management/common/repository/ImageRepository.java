package garlicbears.quiz.domain.management.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import garlicbears.quiz.domain.common.entity.Image;
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	Image findByImageId(Long imageId);
}
