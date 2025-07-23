package com.example.question_service.mapper;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.entity.Question;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {
    Question toQuestion(QuestionCreateRequest request);

    QuestionResponse toQuestionResponse(Question question);
    List<QuestionResponse> toQuestionResponses(List<Question> questions);
    void updateQuestion(QuestionUpdateRequest request, @MappingTarget Question question);
}