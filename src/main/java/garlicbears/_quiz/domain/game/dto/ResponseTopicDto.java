package garlicbears._quiz.domain.game.dto;

import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.global.entity.Active;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTopicDto {
    private long topicId;
    private String topicText;
    private Active topicStatus;
    private LocalDateTime topicCreationDate;
    private LocalDateTime topicUpdateDate;
    private int topicUsageCount;

    public static ResponseTopicDto fromTopic(Topic topic) {
        return ResponseTopicDto.builder()
                .topicId(topic.getTopicSeq())
                .topicText(topic.getTopicTitle())
                .topicStatus(topic.getTopicActive())
                .topicCreationDate(topic.getCreatedAt())
                .topicUpdateDate(topic.getUpdatedAt())
                .topicUsageCount(topic.getTopicUsageCount())
                .build();
    }
}
