package garlicbears.quiz.domain.game.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import garlicbears.quiz.domain.common.repository.LogRepository;
import garlicbears.quiz.domain.common.repository.RatingRepository;
import garlicbears.quiz.domain.game.admin.dto.GameStatDto;
import garlicbears.quiz.domain.game.admin.dto.GameStatListDto;
import garlicbears.quiz.domain.game.admin.dto.RatingStatDto;
import garlicbears.quiz.domain.game.admin.dto.ResponseTopicDto;
import garlicbears.quiz.domain.game.admin.dto.TopicPlayTimeDto;
import garlicbears.quiz.domain.game.admin.dto.VisitorCountDto;
import garlicbears.quiz.domain.game.admin.dto.VisitorCountListDto;
import garlicbears.quiz.domain.game.common.repository.GameSessionRepository;
import garlicbears.quiz.domain.game.common.repository.UserAnswerRepository;

@Service
public class GameStatService {
	private final RatingRepository ratingRepository;
	private final GameSessionRepository gameSessionRepository;
	private final UserAnswerRepository userAnswerRepository;
	private final LogRepository logRepository;

	@Autowired
	public GameStatService(RatingRepository ratingRepository, GameSessionRepository gameSessionRepository,
		UserAnswerRepository userAnswerRepository, LogRepository logRepository) {
		this.ratingRepository = ratingRepository;
		this.gameSessionRepository = gameSessionRepository;
		this.userAnswerRepository = userAnswerRepository;
		this.logRepository = logRepository;
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
		// 정렬 기준 순으로 주제별 전체 게임 수, 전체 정답 수 반환
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<GameStatDto> page = gameSessionRepository.calulateGameStat(sort, pageable);

		List<GameStatDto> content = page.getContent();

		// 각 주제별 정상 종료 게임 수, 총 플레이 시간 반환
		for (GameStatDto gameStatDto : content) {
			TopicPlayTimeDto topicPlayTimeDto= userAnswerRepository.getPlayTime(gameStatDto.getTopicId());
			if (topicPlayTimeDto != null)
			{
				gameStatDto.setCompleteCountAndTotalPlayTime(topicPlayTimeDto);
			}
			// 조회한 데이터를 바탕으로 게임 완료율, 게임당 평균 소요시간, 정답률 계산
			gameStatDto.calculate();
		}

		return new GameStatListDto(sort, pageNumber, pageSize, page.getTotalPages(), page.getTotalElements(), content);
	}

	public VisitorCountListDto getDailyVisitors(int pageNumber, int pageSize) {
		// 일일 방문자 수 반환
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<VisitorCountDto> page = logRepository.getDailyVisitors(pageable);


		return new VisitorCountListDto(pageNumber, pageSize, page.getTotalPages(), page.getTotalElements(), page.getContent());
	}

	public VisitorCountListDto getMonthlyVisitors(int pageNumber, int pageSize) {
		// 월별 방문자 수 반환
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<VisitorCountDto> page = logRepository.getMonthlyVisitors(pageable);

		return new VisitorCountListDto(pageNumber, pageSize, page.getTotalPages(), page.getTotalElements(), page.getContent());
	}
}
