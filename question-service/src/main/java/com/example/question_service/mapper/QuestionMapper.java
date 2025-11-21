package com.example.question_service.mapper;

import com.example.question_service.dto.QuestionImportDTO;
import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.entity.Question;
import com.example.question_service.entity.QuestionVersion;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {
    Question toQuestion(QuestionCreateRequest request);
    Question toQuestion(QuestionImportDTO importDTO);
    Question cloneQuestionState(Question question);

    QuestionResponse toQuestionResponse(Question question);
    List<QuestionResponse> toQuestionResponses(List<Question> questions);
    QuestionVersion toQuestionVersionFromCreateRequest(QuestionCreateRequest questionCreateRequest);
    QuestionResponse toQuestionResponseFromVersion(QuestionVersion version);
    @Mapping(target = "id", ignore = true)
    QuestionVersion toQuestionVersion(Question question);
    QuestionVersion toQuestionVersion(QuestionImportDTO importDTO);

    @Mapping(target = "id", ignore = true)
    void updateQuestion(QuestionUpdateRequest request, @MappingTarget Question question);

    @Mapping(target = "id", ignore = true)
    void updateQuestion(Question src, @MappingTarget Question target);
}