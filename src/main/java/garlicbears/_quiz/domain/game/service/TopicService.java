package garlicbears._quiz.domain.game.service;

import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.domain.game.repository.TopicRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    @Transactional
    public void save(String topicTitle) {
        topicRepository.findByTopicTitle(topicTitle).forEach(topic -> {
            if (topic.getTopicActive() == Active.active)
            {
                throw new CustomException(ErrorCode.TOPIC_ALREADY_EXISTS);
            }
        });

        Topic topic = Topic.builder()
                        .topicTitle(topicTitle)
                        .build();

        topicRepository.save(topic);
    }

    public Optional<Topic> findByTopicId(long topicId) {
        return topicRepository.findById(topicId);
    }
}
