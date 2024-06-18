package garlicbears._quiz.domain.game.service;

import garlicbears._quiz.domain.user.dto.UserRankingDto;
import garlicbears._quiz.domain.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final UserRepository userRepository;

    public GameService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserRankingDto> getRankings(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return userRepository.findRankings(pageable).getContent();
    }

    public List<UserRankingDto> getRankingsByTopicId(long topicId, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return userRepository.findRankingsByTopicId(topicId, pageable).getContent();
    }
}
