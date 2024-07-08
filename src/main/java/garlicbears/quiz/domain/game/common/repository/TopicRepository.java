package garlicbears.quiz.domain.game.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import garlicbears.quiz.domain.game.admin.repository.AdminTopicQueryRepository;
import garlicbears.quiz.domain.game.common.entity.Topic;
import garlicbears.quiz.domain.game.user.repository.UserTopicQueryRepository;
@Repository
public interface TopicRepository
	extends JpaRepository<Topic, Long>, UserTopicQueryRepository, AdminTopicQueryRepository {
	List<Topic> findByTopicTitle(String topicTitle);

	Topic findByTopicId(Long topicId);
}
