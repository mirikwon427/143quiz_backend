package garlicbears.quiz.domain.game.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import garlicbears.quiz.domain.game.admin.dto.GameStatDto;

public interface AdminGameSessionQueryRepository {
	Page<GameStatDto> calulateGameStat(String sort, Pageable pageable);
}
