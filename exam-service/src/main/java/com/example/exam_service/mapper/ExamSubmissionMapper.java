package com.example.exam_service.mapper;

import com.example.exam_service.dto.request.ExamSubmissionRequest;
import com.example.exam_service.dto.response.ExamSubmissionResponse;
import com.example.exam_service.entity.Exam;
import com.example.exam_service.entity.ExamSubmission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamSubmissionMapper {
    ExamSubmission toExamSubmission(ExamSubmissionRequest request);
    ExamSubmissionResponse toExamSubmissionResponse(ExamSubmission examSubmission);
}
