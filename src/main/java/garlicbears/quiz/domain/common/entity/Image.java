package garlicbears.quiz.domain.common.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_seq")
	private Long imageId;

	@Column(name = "image_name", nullable = false, length = 200)
	private String imageName;

	@Column(name = "image_origin_name", nullable = false, length = 200)
	private String originName;

	@Lob
	@Column(name = "image_url", nullable = false)
	private String accessUrl;

	public Image() {
	}

	public Image(String originName) {
		this.imageName = getFileName(originName);
		this.originName = originName;
		this.accessUrl = "";
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	//이미지 파일의 확장자를 추출하는 메소드
	public String extractExtension(String originName) {
		int index = originName.lastIndexOf('.');

		return originName.substring(index, originName.length());
	}

	//이미지 파일의 이름을 저장하기 위한 이름으로 변환하는 메소드
	public String getFileName(String originName) {
		return UUID.randomUUID().toString() + "." + extractExtension(originName);
	}

	public Long getImageId() {
		return imageId;
	}

	public String getImageName() {
		return imageName;
	}

	public String getAccessUrl() {
		return accessUrl;
	}
}

