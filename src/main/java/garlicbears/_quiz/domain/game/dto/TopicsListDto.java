package garlicbears._quiz.domain.game.dto;

public class TopicsListDto{

    Long topicId;

    String title;

    public TopicsListDto(){}

    public TopicsListDto(Long topicId, String title){
        this.topicId = topicId;
        this.title = title;
    }

    public Long getTopicId(){
        return topicId;
    }

    public String getTitle(){
        return title;
    }

}
