package garlicbears.quiz.domain.common.entity;

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
	private String url;

	public Long getImageId() {
		return imageId;
	}

}

