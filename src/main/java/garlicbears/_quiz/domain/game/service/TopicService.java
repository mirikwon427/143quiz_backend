package garlicbears._quiz.domain.game.service;

import garlicbears._quiz.domain.game.dto.ResponseTopicDto;
import garlicbears._quiz.domain.game.dto.ResponseTopicListDto;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.domain.game.repository.TopicRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    @Transactional
    public ResponseTopicListDto getTopicList(int pageNumber, int pageSize, String sort) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, sort));
        Page<ResponseTopicDto> page = topicRepository.findAll(pageable).map(ResponseTopicDto::fromTopic);

        return ResponseTopicListDto.fromTopics(
                page.getContent(), sort, pageNumber, pageSize, page.getTotalPages(),
                page.getTotalElements());
    }

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

    public List<Topic> findByTopicTitle(String topicTitle) {
        return topicRepository.findByTopicTitle(topicTitle);
    }
}
