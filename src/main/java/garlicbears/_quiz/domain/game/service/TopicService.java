package garlicbears._quiz.domain.game.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import garlicbears._quiz.domain.admin.dto.TopicExcelUploadedDto;
import garlicbears._quiz.domain.game.dto.ResponseQuestionDto;
import garlicbears._quiz.domain.game.dto.ResponseTopicDto;
import garlicbears._quiz.domain.game.dto.ResponseTopicListDto;
import garlicbears._quiz.domain.game.entity.Question;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.domain.game.repository.TopicRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;
import jakarta.transaction.Transactional;

@Service
public class TopicService {
	private final TopicRepository topicRepository;
	private final QuestionService questionService;

	@Autowired
	public TopicService(TopicRepository topicRepository, QuestionService questionService) {
		this.topicRepository = topicRepository;
		this.questionService = questionService;
	}

	@Transactional
	public ResponseTopicListDto getTopicList(int pageNumber, int pageSize, String sort) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ResponseTopicDto> page = topicRepository.findTopics(pageNumber, pageSize, sort, pageable);

		return ResponseTopicListDto.fromTopics(page.getContent(), sort, pageNumber, pageSize, page.getTotalPages(),
			page.getTotalElements());
	}

	public TopicExcelUploadedDto saveTopicWithExcel(MultipartFile file) {
		// 엑셀 파일을 통해 주제 생성
		String fileName = file.getOriginalFilename();
		if (fileName == null || !fileName.endsWith(".xlsx")) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
		String topicTitle = fileName.substring(0, fileName.length() - 5);

		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
		} catch (Exception e) {
			throw new CustomException(ErrorCode.FILE_IO_ERROR);
		}

		// 기존재 주제의 경우 해당 주제 사용
		// 새로운 주제의 경우 주제 생성
		Topic topic = null;
		try {
			topic = save(topicTitle);
		} catch (CustomException e) {
			for (Topic t : topicRepository.findByTopicTitle(topicTitle)) {
				if (t.getTopicActive() == Active.active) {
					topic = t;
					break;
				}
			}
		}

		if (topic == null) {
			throw new CustomException(ErrorCode.TOPIC_NOT_FOUND);
		}

		// 엑셀 파일의 첫 번째 시트에서 문제 생성
		// 문제 생성 중 오류 발생 시 해당 문제는 생성하지 않음
		List<ResponseQuestionDto> questions = new ArrayList<>();
		Sheet sheet = workbook.getSheetAt(0);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				String questionText = row.getCell(0).getStringCellValue();
				if (questionText == null || questionText.trim().isEmpty()) {
					continue;
				}
				try {
					Question question = questionService.save(topic, questionText); // QuestionService의 save 메서드 호출
					questions.add(new ResponseQuestionDto(topic, question));
				} catch (CustomException e) {
					continue;
				}
			}
		}

		try {
			workbook.close();
		} catch (Exception e) {
			throw new CustomException(ErrorCode.FILE_IO_ERROR);
		}

		return new TopicExcelUploadedDto(topic, questions);
	}

	@Transactional
	public Topic save(String topicTitle) {
		topicRepository.findByTopicTitle(topicTitle).forEach(topic -> {
			if (topic.getTopicActive() == Active.active) {
				throw new CustomException(ErrorCode.TOPIC_ALREADY_EXISTS);
			}
		});

		Topic topic = new Topic(topicTitle);

		return topicRepository.save(topic);
	}

	@Transactional
	public void update(long topicId, String topicTitle) {
		Topic topic = topicRepository.findById(topicId)
			.orElseThrow(() -> new CustomException(ErrorCode.TOPIC_NOT_FOUND));

		topic.setTopicTitle(topicTitle);

		topicRepository.save(topic);
	}

	@Transactional
	public void delete(long topicId) {
		Topic topic = topicRepository.findById(topicId)
			.orElseThrow(() -> new CustomException(ErrorCode.TOPIC_NOT_FOUND));

		topic.setTopicActive(Active.inactive);

		topicRepository.save(topic);

		questionService.deleteByTopic(topic);
	}

	public Optional<Topic> findByTopicId(long topicId) {
		return topicRepository.findById(topicId);
	}

	public List<Topic> findByTopicTitle(String topicTitle) {
		return topicRepository.findByTopicTitle(topicTitle);
	}
}
