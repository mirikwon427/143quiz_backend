package garlicbears.quiz.domain.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears.quiz.domain.common.entity.Log;

public interface LogRepository extends JpaRepository<Log, Long>{
}
