package com.example.exam_service.service.impl;

import com.example.exam_service.dto.request.ExamCreationRequest;
import com.example.exam_service.dto.request.PracticeExamCreationRequest;
import com.example.exam_service.dto.response.ExamResponse;
import com.example.exam_service.dto.response.ExamViewResponse;
import com.example.exam_service.dto.response.ProficiencyPredictionResponse;
import com.example.exam_service.dto.response.QuestionInUnstartedExamCheck;
import com.example.exam_service.dto.response.QuestionResponse;
import com.example.exam_service.entity.Exam;
import com.example.exam_service.entity.ExamQuestion;
import com.example.exam_service.event.AuditLogEvent;
import com.example.exam_service.mapper.ExamMapper;
import com.example.exam_service.repository.ExamQuestionRepository;
import com.example.exam_service.repository.ExamRepository;
import com.example.exam_service.repository.httpClient.QuestionClient;
import com.example.exam_service.service.ExamService;
import com.example.exam_service.service.PredictService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    KafkaTemplate<String, Object> kafkaTemplate;
    PredictService predictService;

    @Override
    public ExamViewResponse createExam(ExamCreationRequest request) {
        try {
            Exam exam = examMapper.toExam(request);
            List<QuestionResponse> questions =
                    questionClient.getRandomQuestions(
                            exam.getSubjectId(), exam.getNumberOfQuestion(), 0.2, 0.5, 0.3
                    ).getResult();

            exam = examRepository.save(exam);

            createExamQuestions(exam, questions);

            AuditLogEvent logEvent = new AuditLogEvent(
                    request.getTeacher(),
                    "TEACHER",
                    "CREATE EXAM",
                    "Created exam with ID: " + exam.getId()
            );
            kafkaTemplate.send("audit.log", logEvent);

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
    public ExamViewResponse createPracticeExam(PracticeExamCreationRequest request) {
        try {
            // Lấy dự đoán năng lực từ ML API
            ProficiencyPredictionResponse prediction = predictService.getProficiencyPrediction(request.getStudent());

            // Normalize ratios để tổng = 1.0 (tránh lỗi rounding)
            double totalRatio = prediction.getEasyRatio() + prediction.getMediumRatio() + prediction.getHardRatio();
            double easyRatio = prediction.getEasyRatio() / totalRatio;
            double mediumRatio = prediction.getMediumRatio() / totalRatio;
            double hardRatio = prediction.getHardRatio() / totalRatio;

            log.info("Creating practice exam for student: {} with ratios - Easy: {}, Medium: {}, Hard: {}",
                    request.getStudent(), easyRatio, mediumRatio, hardRatio);

            // Tạo exam entity từ request
            Exam exam = examMapper.toExamFromPracticeRequest(request);

            // Lấy câu hỏi dựa trên tỉ lệ dự đoán (đã normalize)
            List<QuestionResponse> questions = questionClient.getRandomQuestions(
                    exam.getSubjectId(),
                    exam.getNumberOfQuestion(),
                    hardRatio,
                    mediumRatio,
                    easyRatio
            ).getResult();

            // Lưu exam
            exam = examRepository.save(exam);

            // Tạo exam questions
            createExamQuestions(exam, questions);

            // Gửi audit log
            AuditLogEvent logEvent = new AuditLogEvent(
                    request.getStudent(),
                    "STUDENT",
                    "CREATE PRACTICE EXAM",
                    String.format("Created practice exam with ID: %d (Easy: %.2f, Medium: %.2f, Hard: %.2f)",
                            exam.getId(), easyRatio, mediumRatio, hardRatio)
            );
            kafkaTemplate.send("audit.log", logEvent);

            return ExamViewResponse.builder()
                    .exam(exam)
                    .questions(questions)
                    .build();

        } catch (Exception e) {
            log.error("Lỗi khi tạo bài thi luyện tập cho học sinh {}: {}", request.getStudent(), e.getMessage(), e);
            throw new RuntimeException("Không thể tạo bài thi luyện tập, vui lòng thử lại sau.");
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
    public List<ExamResponse> getExamsByStudent(String student) {
        List<Exam> exams = examRepository.findExamByTeacher(student);
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
