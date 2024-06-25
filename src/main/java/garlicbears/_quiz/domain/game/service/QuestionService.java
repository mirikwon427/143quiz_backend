package garlicbears._quiz.domain.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import garlicbears._quiz.domain.game.dto.ResponseQuestionDto;
import garlicbears._quiz.domain.game.dto.ResponseQuestionListDto;
import garlicbears._quiz.domain.game.entity.Question;
import garlicbears._quiz.domain.game.entity.Topic;
import garlicbears._quiz.domain.game.repository.QuestionRepository;
import garlicbears._quiz.global.entity.Active;
import garlicbears._quiz.global.exception.CustomException;
import garlicbears._quiz.global.exception.ErrorCode;

@Service
public class QuestionService {
	private final QuestionRepository questionRepository;

	// 초성 19자
	private final String[] initialChs = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ",
		"ㅌ", "ㅍ", "ㅎ"};

	@Autowired
	public QuestionService(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}

	@Transactional
	public ResponseQuestionListDto getQuestionList(int pageNumber, int pageSize, String sort) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ResponseQuestionDto> page = questionRepository.findQuestions(pageNumber, pageSize, sort, pageable);

		return new ResponseQuestionListDto(page.getContent(), sort, pageNumber, pageSize, page.getTotalPages(),
			page.getTotalElements());
	}

	@Transactional
	public ResponseQuestionListDto getQuestionListByTopic(Topic topic, int pageNumber, int pageSize, String sort) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ResponseQuestionDto> page = questionRepository.findQuestionsByTopicId(topic.getTopicId(), pageNumber,
			pageSize, sort, pageable);

		return new ResponseQuestionListDto(page.getContent(), sort, pageNumber, pageSize, page.getTotalPages(),
			page.getTotalElements());
	}

	private String createInitialQuestion(String questionAnswerText) {
		StringBuilder questionText = new StringBuilder();

		// 한글 이외의 문자가 포함되어 있을 경우 에러(400) 반환
		for (char c : questionAnswerText.toCharArray()) {
			if (c < '가' || c > '힣') {
				throw new CustomException(ErrorCode.INVALID_INPUT);
			}
			int initial = (((c - '가') / 28) / 21);
			questionText.append(initialChs[initial]);
		}

		return questionText.toString();
	}

	@Transactional
	public Question save(Topic topic, String questionAnswerText) {
		// 이미 등록된 문제인 경우 에러(409) 반환
		questionRepository.findByQuestionAnswerText(questionAnswerText).forEach(question -> {
			if (question.getQuestionActive() == Active.active) {
				throw new CustomException(ErrorCode.QUESTION_ALREADY_EXISTS);
			}
		});

		String questionText = createInitialQuestion(questionAnswerText);

		Question question = new Question(topic, questionText, questionAnswerText);

		return questionRepository.save(question);
	}

	@Transactional
	public void update(long questionId, String questionAnswerText) {
		// 문제가 존재하지 않는 경우 에러(404) 반환
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		question.setQuestionText(createInitialQuestion(questionAnswerText));
		question.setQuestionAnswerText(questionAnswerText);

		questionRepository.save(question);
	}

	@Transactional
	public void delete(long questionId) {
		// 문제가 존재하지 않는 경우 에러(404) 반환
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		question.setQuestionActive(Active.inactive);

		questionRepository.save(question);
	}

	@Transactional
	public void deleteByTopic(Topic topic) {
		// 토픽이 삭제된 경우 관련 문제 모두 삭제
		questionRepository.deleteByTopic(topic);
	}
}
