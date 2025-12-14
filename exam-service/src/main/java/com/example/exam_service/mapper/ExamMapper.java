package com.example.exam_service.mapper;

import com.example.exam_service.dto.request.ExamCreationRequest;
import com.example.exam_service.dto.request.PracticeExamCreationRequest;
import com.example.exam_service.dto.response.ExamResponse;
import com.example.exam_service.entity.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    Exam toExam(ExamCreationRequest request);

    @Mapping(source = "student", target = "teacher")
    Exam toExamFromPracticeRequest(PracticeExamCreationRequest request);

    ExamResponse toCoExamResponse(Exam exam);
    List<ExamResponse> toExamResponseList(List<Exam> exams);
}
