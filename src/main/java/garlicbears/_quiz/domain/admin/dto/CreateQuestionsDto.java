package garlicbears._quiz.domain.admin.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
