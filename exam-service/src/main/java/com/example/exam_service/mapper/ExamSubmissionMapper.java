package com.example.exam_service.mapper;

import com.example.exam_service.dto.request.ExamSubmissionRequest;
import com.example.exam_service.dto.response.AnswerResponse;
import com.example.exam_service.dto.response.ExamSubmissionResponse;
import com.example.exam_service.dto.response.QuestionResponse;
import com.example.exam_service.entity.Exam;
import com.example.exam_service.entity.ExamSubmission;
import com.example.exam_service.entity.ExamSubmissionAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamSubmissionMapper {
    ExamSubmission toExamSubmission(ExamSubmissionRequest request);
    ExamSubmissionResponse toExamSubmissionResponse(ExamSubmission examSubmission);
    List<ExamSubmissionResponse> toExamSubmissionsResponse(List<ExamSubmission> examSubmissions);
    List<AnswerResponse> toAnswersResponse(List<ExamSubmissionAnswer> answers);

    @Mapping(source = "answer.id", target = "id")
    @Mapping(source = "answer.questionId", target = "questionId")
    @Mapping(source = "answer.selectedOption", target = "selectedOption")

    @Mapping(source = "question.content", target = "content")
    @Mapping(source = "question.optionA", target = "optionA")
    @Mapping(source = "question.optionB", target = "optionB")
    @Mapping(source = "question.optionC", target = "optionC")
    @Mapping(source = "question.optionD", target = "optionD")
    @Mapping(source = "question.correctAnswer", target = "correctAnswer")
    AnswerResponse toAnswerResponse(ExamSubmissionAnswer answer, QuestionResponse question);
}
