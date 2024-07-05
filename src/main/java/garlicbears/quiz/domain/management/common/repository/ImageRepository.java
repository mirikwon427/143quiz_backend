package garlicbears.quiz.domain.management.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears.quiz.domain.common.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	Image findByImageId(Long imageId);
}
