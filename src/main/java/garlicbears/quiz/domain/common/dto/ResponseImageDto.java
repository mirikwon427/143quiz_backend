package garlicbears.quiz.domain.common.dto;

public class ResponseImageDto {
	private String imageUrl;

	public ResponseImageDto(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
