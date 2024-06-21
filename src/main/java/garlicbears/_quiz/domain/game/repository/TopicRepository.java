package garlicbears._quiz.domain.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import garlicbears._quiz.domain.game.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long>, TopicQueryRepository {
	List<Topic> findByTopicTitle(String topicTitle);

	Topic findByTopicId(Long topicId);

}
