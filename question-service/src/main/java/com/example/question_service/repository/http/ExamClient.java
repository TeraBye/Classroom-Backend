package com.example.question_service.repository.http;

import com.example.question_service.dto.response.ApiResponse;
import com.example.question_service.dto.response.QuestionInUnstartedExamCheck;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exam-service", url = "app.services.exam")
public interface ExamClient {
    @GetMapping("/{questionId}/isQuestionInUnstartedExam")
    ApiResponse<QuestionInUnstartedExamCheck> isQuestionInUnstartedExam(
            @PathVariable int questionId
    );
}
