package garlicbears._quiz.domain.game.dto;

import java.util.List;

public class ResponseTopicBadgeDto {

	private List<TopicsListDto> topics;

	public ResponseTopicBadgeDto() {
	}

	public ResponseTopicBadgeDto(List<TopicsListDto> topics) {
		this.topics = topics;
	}

	public List<TopicsListDto> getTopics() {
		return topics;
	}

}
