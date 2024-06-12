package garlicbears._quiz.domain.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateQuestionsDto {
    @NotNull
    @Size(max = 100)
    private String topicTitle;

    private List<String> questions;
}
