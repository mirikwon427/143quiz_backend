package garlicbears._quiz.domain.admin.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateQuestionsDto {
    private String topicTitle;

    private List<String> questions;
}
