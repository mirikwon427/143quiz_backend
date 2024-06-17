package garlicbears._quiz.domain.game.repository;

import garlicbears._quiz.domain.game.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long>, TopicQueryRepository {
    List<Topic> findByTopicTitle(String topicTitle);
}
