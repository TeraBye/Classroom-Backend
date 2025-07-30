package com.example.question_service.service.Impl;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.ClassListResponse;
import com.example.question_service.dto.response.QuestionPagingResponse;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.dto.response.SubjectResponse;
import com.example.question_service.entity.Question;
import com.example.question_service.exception.BusinessException;
import com.example.question_service.mapper.QuestionMapper;
import com.example.question_service.repository.QuestionRepository;
import com.example.question_service.repository.http.ClassroomClient;
import com.example.question_service.service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements QuestionService {
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    ClassroomClient classroomClient;
    @Override
    public QuestionResponse createQuestion(QuestionCreateRequest request) {
        if (!questionRepository.existsByContentIgnoreCase(request.getContent())) {
            Question question = questionMapper.toQuestion(request);
            question.setCreatedAt(LocalDateTime.now());
            return questionMapper.toQuestionResponse(questionRepository.save(question));
        } else {
            throw new RuntimeException("Question existed");
        }
    }

    @Override
    public QuestionResponse getQuestionById(int questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));
        return questionMapper.toQuestionResponse(question);
    }

    @Override
    public List<QuestionResponse> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questionMapper.toQuestionResponses(questions);
    }

    @Override
    public QuestionResponse updateQuestion(int questionId, QuestionUpdateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));
        questionMapper.updateQuestion(request,question);
        question.setUpdatedAt(LocalDateTime.now());
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    @Override
    public void deleteQuestion(int questionId) {
        questionRepository.deleteById(questionId);
    }

    @Override
    public List<QuestionResponse> getQuestionsBySubjectId(int subjectId) {
        List<Question> questions = questionRepository.findBySubjectId(subjectId);
        return questionMapper.toQuestionResponses(questions);
    }

    @Override
    public QuestionPagingResponse<QuestionResponse> getPageQuestion(Integer subjectId, int cursor, Pageable pageable) {
        List<Question> questionList = questionRepository.findNextPageScore(subjectId, cursor, pageable);

        boolean hasNext = questionList.size() == pageable.getPageSize();
        int lastCursor = questionList.isEmpty() ? cursor :questionList.getLast().getId();

        return new QuestionPagingResponse<>(questionMapper.toQuestionResponses(questionList), lastCursor, hasNext);
    }

    @Override
    public List<ClassListResponse> getSubjectList(Integer subjectId, int cursor, Pageable pageable) {
        List<Integer> listSubject;
        List<SubjectResponse> subjectResponses;

        if (subjectId == -999){
            listSubject = questionRepository.findAllNextPageSubjectQuestion(cursor, pageable);
        } else {
            listSubject = questionRepository.findDistinctNextPageListSubject(subjectId, cursor, pageable);
        }

        try {
            subjectResponses = classroomClient.getListSubject(listSubject).getResult();
        } catch (Exception e){
            throw new BusinessException("Can not get classes name: " + e.getMessage());
        }

        if (listSubject != null && subjectResponses != null){
            List<ClassListResponse> listResponses = new ArrayList<>();

            for (SubjectResponse subject: subjectResponses) {
                int total = questionRepository.countBySubjectId(subject.getId());
                String subjectName = subject.getName();
                listResponses.add(new ClassListResponse(subject.getId(), subjectName, total));
            }
            return listResponses;
        }
        throw new BusinessException("Can not find any subject!");


    }

}
