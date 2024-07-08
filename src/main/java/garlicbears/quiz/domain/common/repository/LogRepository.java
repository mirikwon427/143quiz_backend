package garlicbears.quiz.domain.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import garlicbears.quiz.domain.common.entity.Log;
import garlicbears.quiz.domain.game.admin.repository.AdminLogQueryRepository;
@Repository
public interface LogRepository extends JpaRepository<Log, Long>, AdminLogQueryRepository {
}
