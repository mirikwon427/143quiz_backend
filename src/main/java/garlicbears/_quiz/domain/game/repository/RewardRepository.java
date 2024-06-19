package garlicbears._quiz.domain.game.repository;

import garlicbears._quiz.domain.game.entity.Reward;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward,Long> {
    Optional<Reward> findByUserAndTopic(User user, Topic topic);

    @Query("SELECT r.topic.topicId FROM Reward r WHERE r.user = :user AND r.rewardBadgeStatus = true")
    List<Long> findTopicIdsWithBadgeByUser(@Param("user") User user);
}
