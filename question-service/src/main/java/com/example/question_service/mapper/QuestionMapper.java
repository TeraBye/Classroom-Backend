package com.example.question_service.mapper;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.entity.Question;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {
    Question toQuestion(QuestionCreateRequest request);

    QuestionResponse toQuestionResponse(Question question);
    List<QuestionResponse> toQuestionResponses(List<Question> questions);
    @Mapping(target = "id", ignore = true)
    void updateQuestion(QuestionUpdateRequest request, @MappingTarget Question question);
}