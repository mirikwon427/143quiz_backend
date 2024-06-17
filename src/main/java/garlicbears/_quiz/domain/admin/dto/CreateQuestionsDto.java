package garlicbears._quiz.domain.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateQuestionsDto {
    @NotNull
    @Size(max = 100)
    private String topicTitle;

    private List<String> questions;

    public String getTopicTitle() {
        return topicTitle;
    }

    public List<String> getQuestions() {
        return questions;
    }
}
