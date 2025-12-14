package com.example.exam_service.repository.httpClient;

import com.example.exam_service.dto.request.PredictRequest;
import com.example.exam_service.dto.response.ProficiencyPredictionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ml-predict-service", url = "http://localhost:8000")
public interface PredictClient {

    @PostMapping(value = "/predict", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ProficiencyPredictionResponse predict(@RequestBody PredictRequest request);
}

