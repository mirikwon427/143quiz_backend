package garlicbears._quiz.domain.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseTopicBadgeDto {

    private int status;

    private List<TopicsListDto> topics;

    public ResponseTopicBadgeDto(){}

    public ResponseTopicBadgeDto(int status, List<TopicsListDto> topics) {
        this.status = status;
        this.topics = topics;
    }

    public int getStatus() {
        return status;
    }

    public List<TopicsListDto> getTopics() {
        return topics;
    }

}
