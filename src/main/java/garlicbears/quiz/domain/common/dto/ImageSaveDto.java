package garlicbears.quiz.domain.common.dto;

import org.springframework.web.multipart.MultipartFile;

public class ImageSaveDto {
	private MultipartFile image;

	public ImageSaveDto() {
	}

	public ImageSaveDto(MultipartFile image) {
		this.image = image;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
}
