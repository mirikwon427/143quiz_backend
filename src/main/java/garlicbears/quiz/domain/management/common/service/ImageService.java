package garlicbears.quiz.domain.management.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import garlicbears.quiz.domain.common.entity.Image;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;

public class ImageService {
	@Value("${cloud.aws.s3.bucket}")
	private static String bucketName;

	private final AmazonS3Client amazonS3Client;
	private final ImageRepository imageRepository;

	public ImageService(AmazonS3Client amazonS3Client, ImageRepository imageRepository) {
		this.amazonS3Client = amazonS3Client;
		this.imageRepository = imageRepository;
	}

	@Transactional
	public Image saveImage(MultipartFile multipartFile) {
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
}
