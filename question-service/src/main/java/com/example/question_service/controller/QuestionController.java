package com.example.question_service.controller;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionIdsRequest;
import com.example.question_service.dto.request.QuestionSearchRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.ApiResponse;
import com.example.question_service.dto.response.ClassListResponse;
import com.example.question_service.dto.response.QuestionPagingResponse;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.service.QuestionHistoryService;
import com.example.question_service.service.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {
    QuestionService questionService;
    QuestionHistoryService questionHistoryService;

    @PostMapping("/create")
    public ApiResponse<QuestionResponse> createQuestion(@RequestBody @Valid QuestionCreateRequest request) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.createQuestion(request))
                .build();
    }

    @PutMapping("/{questionId}")
    public ApiResponse<QuestionResponse> updateQuestion(@PathVariable int questionId, @RequestBody QuestionUpdateRequest request) throws JsonProcessingException {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.updateQuestion(questionId, request))
                .build();
    }

    @DeleteMapping("/{username}/{questionId}/delete")
    public ApiResponse<Void> deleteQuestion(@PathVariable("username") String username, @PathVariable("questionId") int questionId) {
        questionService.deleteQuestion(questionId, username);
        return ApiResponse.<Void>builder()
                .message("Delete question successfully")
                .build();
    }

    @PostMapping("/{username}/undo")
    public ApiResponse<String> undo(@PathVariable("username") String username) {
        return ApiResponse.<String>builder()
                .result(questionService.undo(username))
                .build();
    }

    @PostMapping("/{username}/redo")
    public ApiResponse<String> redo(@PathVariable("username") String username) {
        return ApiResponse.<String>builder()
                .result(questionService.redo(username))
                .build();
    }

    @GetMapping("/{username}/canUndo")
    public ApiResponse<Boolean> canUndo(@PathVariable String username) {
        return ApiResponse.<Boolean>builder()
                .result(questionHistoryService.canUndo(username))
                .build();
    }

    @GetMapping("/{username}/canRedo")
    public ApiResponse<Boolean> canRedo(@PathVariable String username) {
        return ApiResponse.<Boolean>builder()
                .result(questionHistoryService.canRedo(username))
                .build();
    }

    @GetMapping("/get-by-subject/{subjectId}")
    public ApiResponse<QuestionPagingResponse<QuestionResponse>> getQuestionBySubjectId(
            @PathVariable int subjectId,
            QuestionSearchRequest request,
            Pageable pageable) {
        return ApiResponse.<QuestionPagingResponse<QuestionResponse>>builder()
                .result(questionService.getPageQuestion(subjectId, request, pageable))
                .build();
    }


    //Luan lam
    @GetMapping("/random")
    public ApiResponse<List<QuestionResponse>> getRandomQuestions(
            @RequestParam Integer subjectId, @RequestParam Integer n, @RequestParam Float hardRatio, @RequestParam Float mediumRatio, @RequestParam Float easyRatio) {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getRandomQuestionsBySubject(subjectId,n,hardRatio,mediumRatio,easyRatio))
                .build();

    }

    @PostMapping("/getQuestions-by-ids")
    public ApiResponse<List<QuestionResponse>> getQuestionsByIds(@RequestBody QuestionIdsRequest request) {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getQuestionByIds(request))
                .build();

    }

    @GetMapping("/get-list-question")
    public ApiResponse<QuestionPagingResponse<QuestionResponse>> getListUsers(
            @RequestParam Integer subjectId,
            @RequestParam int cursor,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page,size);
        return ApiResponse.<QuestionPagingResponse<QuestionResponse>>builder()
                .result(questionService.getPageQuestion(subjectId, cursor, pageable))
                .build();
    }

    @GetMapping("get-subjects")
    public ApiResponse<List<ClassListResponse>> getListSubject(
            @RequestParam (defaultValue = "-999") Integer subjectId,
            @RequestParam int cursor,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "15") int size
    ){
        Pageable pageable = PageRequest.of(page,size);

        return ApiResponse.<List<ClassListResponse>>builder()
                .result(questionService.getSubjectList(subjectId,cursor,pageable)).build();
    }

    @DeleteMapping("/{questionId}")
    public ApiResponse<Void> deleteQuestion( @PathVariable("questionId") int questionId) {
        questionService.deleteQuestionById(questionId);
        return ApiResponse.<Void>builder()
                .message("Delete question successfully")
                .build();
    }
}
