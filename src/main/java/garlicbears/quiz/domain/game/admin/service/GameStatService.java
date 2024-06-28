package garlicbears.quiz.domain.game.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import garlicbears.quiz.domain.common.repository.RatingRepository;
import garlicbears.quiz.domain.game.admin.dto.GameStatDto;
import garlicbears.quiz.domain.game.admin.dto.GameStatListDto;
import garlicbears.quiz.domain.game.admin.dto.RatingStatDto;
import garlicbears.quiz.domain.game.admin.dto.ResponseTopicDto;
import garlicbears.quiz.domain.game.common.repository.GameSessionRepository;

@Service
public class GameStatService {
	private final RatingRepository ratingRepository;
	private final GameSessionRepository gameSessionRepository;

	@Autowired
	public GameStatService(RatingRepository ratingRepository, GameSessionRepository gameSessionRepository) {
		this.ratingRepository = ratingRepository;
		this.gameSessionRepository = gameSessionRepository;
	}

	public RatingStatDto getRatingStat() {
		// 별점 평균 반환
		Double averageRating = ratingRepository.getAverageRating();
		if (averageRating == null) {
			return new RatingStatDto(0, 0);
		}
		return new RatingStatDto(averageRating, ratingRepository.count());
	}

	public GameStatListDto getGameStatList(int pageNumber, int pageSize, String sort) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<GameStatDto> page = gameSessionRepository.calulateGameStat(sort, pageable);

		return new GameStatListDto(sort, pageNumber, pageSize, page.getTotalPages(), page.getTotalElements(), page.getContent());
	}
}
