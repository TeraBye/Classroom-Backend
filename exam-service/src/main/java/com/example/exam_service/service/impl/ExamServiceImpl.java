package com.example.exam_service.service.impl;

import com.example.exam_service.dto.request.ExamCreationRequest;
import com.example.exam_service.dto.response.ExamResponse;
import com.example.exam_service.dto.response.ExamViewResponse;
import com.example.exam_service.dto.response.QuestionInUnstartedExamCheck;
import com.example.exam_service.dto.response.QuestionResponse;
import com.example.exam_service.entity.Exam;
import com.example.exam_service.entity.ExamQuestion;
import com.example.exam_service.mapper.ExamMapper;
import com.example.exam_service.repository.ExamQuestionRepository;
import com.example.exam_service.repository.ExamRepository;
import com.example.exam_service.repository.httpClient.QuestionClient;
import com.example.exam_service.service.ExamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExamServiceImpl implements ExamService {
    ExamRepository examRepository;
    ExamQuestionRepository examQuestionRepository;
    ExamMapper examMapper;
    QuestionClient questionClient;

    @Override
    public ExamViewResponse createExam(ExamCreationRequest request) {
        try {
            Exam exam = examMapper.toExam(request);
            List<QuestionResponse> questions =
                    questionClient.getRandomQuestions(
                            exam.getSubjectId(), exam.getNumberOfQuestion()
                    ).getResult();

            exam = examRepository.save(exam);

            createExamQuestions(exam, questions);

            return ExamViewResponse.builder()
                    .exam(exam)
                    .questions(questions)
                    .build();

        } catch (Exception e) {
            log.error("Lỗi khi tạo exam: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tạo bài kiểm tra, vui lòng thử lại sau.");
        }
    }


    @Override
    public void createExamQuestions(Exam exam, List<QuestionResponse> questionList) {
        List<ExamQuestion> examQuestions = new ArrayList<>();

        for (int i = 0; i < questionList.size(); i++) {
            QuestionResponse qr = questionList.get(i);

            ExamQuestion eq = new ExamQuestion();
            eq.setExam(exam);                          // gán thực thể exam
            eq.setQuestionId(qr.getId());              // gán id của câu hỏi
            eq.setOrderNo(i + 1);                      // đánh số thứ tự câu

            examQuestions.add(eq);
        }
        examQuestionRepository.saveAll(examQuestions);
    }

    @Override
    public List<ExamResponse> getExamsByClass(int classId) {
        List<Exam> exams = examRepository.findExamByClassId(classId);
        return examMapper.toExamResponseList(exams);
    }

    @Override
    public QuestionInUnstartedExamCheck isQuestionInUnstartedExam(int questionId) {
        List<ExamQuestion> unstartedExams = examQuestionRepository.findUnstartedExamsByQuestionId(questionId, LocalDateTime.now());
        return QuestionInUnstartedExamCheck.builder()
                .isInUnstartedExam(!unstartedExams.isEmpty())
                .build();
    }


}
