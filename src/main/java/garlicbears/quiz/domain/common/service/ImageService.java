package garlicbears.quiz.domain.common.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import garlicbears.quiz.domain.common.entity.Image;
import garlicbears.quiz.domain.common.entity.User;
import garlicbears.quiz.domain.game.common.entity.Topic;
import garlicbears.quiz.domain.management.common.repository.ImageRepository;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

@Service
public class ImageService {
	private static final Logger logger = Logger.getLogger(ImageService.class.getName());
	@Value("${cloud.aws.s3.bucketName}")
	private String bucketName;

	private final AmazonS3Client amazonS3Client;
	private final ImageRepository imageRepository;

	public ImageService(AmazonS3Client amazonS3Client, ImageRepository imageRepository) {
		this.amazonS3Client = amazonS3Client;
		this.imageRepository = imageRepository;
	}

	@Transactional
	// public Image processImage(Object container, MultipartFile newImageFile, Long defaultImageId) {
	// 	Image currentImage = null;
	//
	// 	if (container instanceof User user) {
	// 		currentImage = user.getImage();
	// 	} else if (container instanceof Topic topic) {
	// 		currentImage = topic.getTopicImage();
	// 	}
	//
	// 	Long currentImageId = (currentImage != null) ? currentImage.getImageId() : null;
	//
	// 	// 기본 이미지는 삭제하지 않음
	// 	if (currentImageId != null && !currentImageId.equals(defaultImageId)) {
	// 		if (container instanceof User) {
	// 			((User) container).setImage(null);
	// 		} else {
	// 			((Topic) container).setTopicImage(null);
	// 		}
	//
	// 		deleteS3Image(currentImageId);
	// 	}
	//
	// 	// AWS S3와 DB에 새 이미지 정보 저장
	// 	Image newImage = saveImage(newImageFile);
	//
	// 	if (container instanceof User) {
	// 		((User) container).setImage(newImage);
	// 	} else if (container instanceof Topic) {
	// 		((Topic) container).setTopicImage(newImage);
	// 	}
	//
	// 	return newImage;
	// }
	public Image processImage(MultipartFile newImageFile, Long defaultImageId) {
		Image currentImage = null;


		// AWS S3와 DB에 새 이미지 정보 저장
		Image newImage = saveImage(newImageFile);


		return newImage;
	}
	@Transactional
	public Image saveImage(MultipartFile multipartFile) {
		logger.info(multipartFile.getOriginalFilename());
		String originName = multipartFile.getOriginalFilename();
		Image image = new Image(originName);
		String imageName = image.getImageName();

		try {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(multipartFile.getContentType());
			objectMetadata.setContentLength(multipartFile.getInputStream().available());

			// S3에 이미지 업로드
			amazonS3Client.putObject(bucketName, imageName, multipartFile.getInputStream(), objectMetadata);

			// S3에서 이미지 접근 URL 가져오기
			String accessUrl = amazonS3Client.getUrl(bucketName, imageName).toString();
			image.setAccessUrl(accessUrl);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}

		imageRepository.save(image);
		return image;
	}

	@Transactional
	public void deleteS3Image(Long imageId) {
		Image image = imageRepository.findByImageId(imageId);
		if (image == null) {
			throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
		}

		String imageName = image.getImageName();
		try {
			amazonS3Client.deleteObject(bucketName, imageName);
			//DB에서 이미지 삭제
			deleteDBImage(imageId);
		} catch (Exception e) {
			logger.warning("Unexpected AWS S3 deleting imageId : " + e.getMessage());
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public void deleteDBImage(Long imageId) {
		try {
			Image image = imageRepository.findByImageId(imageId);
			if (image == null) {
				logger.warning("ImageId not found : " + imageId);
				throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
			}
			imageRepository.delete(image);
		} catch (Exception e) {
			logger.warning("Unexpected DB deleting imageId : " + e.getMessage());
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
