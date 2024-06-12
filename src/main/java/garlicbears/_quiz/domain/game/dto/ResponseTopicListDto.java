package garlicbears._quiz.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTopicListDto {
    private String sort;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private long totalCount;
    private List<ResponseTopicDto> topics;

    public static ResponseTopicListDto fromTopics(List<ResponseTopicDto> topics, String sort
            , int pageNumber, int pageSize, int totalPage, long totalCount) {
        return ResponseTopicListDto.builder()
                .sort(sort)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .topics(topics)
                .build();
    }
}
