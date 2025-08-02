package com.example.question_service.controller;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionIdsRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.ApiResponse;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.service.QuestionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {
    QuestionService questionService;

    @PostMapping("/create")
    public ApiResponse<QuestionResponse> createQuestion(@RequestBody @Valid QuestionCreateRequest request) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.createQuestion(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<QuestionResponse>> getAllQuestions() {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getAllQuestions())
                .build();
    }

    @GetMapping("/{questionId}")
    public ApiResponse<QuestionResponse> getQuestionById(@PathVariable int questionId) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.getQuestionById(questionId))
                .build();
    }

    @PutMapping("/{questionId}")
    public ApiResponse<QuestionResponse> updateQuestion(@PathVariable int questionId, @RequestBody QuestionUpdateRequest request) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.updateQuestion(questionId, request))
                .build();
    }

    @DeleteMapping("/{questionId}")
    public ApiResponse<Void> deleteQuestion(@PathVariable int questionId) {
        questionService.deleteQuestion(questionId);
        return ApiResponse.<Void>builder()
                .message("Delete question successfully")
                .build();
    }
    @GetMapping("/get-by-subject/{subjectId}")
    public ApiResponse<List<QuestionResponse>> getQuestionBySubjectId(@PathVariable int subjectId) {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getQuestionsBySubjectId(subjectId))
                .build();
    }
    //Luan lam
    @GetMapping("/random")
    public ApiResponse<List<QuestionResponse>> getRandomQuestions(
            @RequestParam Integer subjectId, @RequestParam Integer n) {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getRandomQuestionsBySubject(subjectId,n))
                .build();

    }

    @PostMapping("/getQuestions-by-ids")
    public ApiResponse<List<QuestionResponse>> getQuestionsByIds(@RequestBody QuestionIdsRequest request) {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getQuestionByIds(request))
                .build();

    }
}
