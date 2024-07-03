package garlicbears.quiz.domain.management.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears.quiz.domain.common.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
