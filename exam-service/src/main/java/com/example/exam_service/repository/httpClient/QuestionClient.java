package com.example.exam_service.repository.httpClient;

import com.example.exam_service.dto.request.QuestionIdsRequest;
import com.example.exam_service.dto.response.ApiResponse;
import com.example.exam_service.dto.response.QuestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "question-service", url = "${app.services.question}")
public interface QuestionClient {
    @GetMapping(value = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<QuestionResponse>> getRandomQuestions(
            @RequestParam Integer subjectId, @RequestParam Integer n, @RequestParam Double hardRatio, @RequestParam Double mediumRatio, @RequestParam Double easyRatio);

    @PostMapping(value = "/getQuestions-by-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<QuestionResponse>> getQuestionsByIds(@RequestBody QuestionIdsRequest request);
}
