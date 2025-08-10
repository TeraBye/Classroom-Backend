package com.example.post_service.repository.httpClient;

import com.example.post_service.dto.request.AssignmentCreateRequest;
import com.example.post_service.dto.request.ListIdRequest;
import com.example.post_service.dto.response.ApiResponse;
import com.example.post_service.dto.response.AssignmentResponse;
import com.example.post_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "assignment-service", url = "${app.services.assignment}")
public interface AssignmentClient {
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<AssignmentResponse> createAssignment(
            @RequestBody AssignmentCreateRequest request);

    @PostMapping(value = "/getAssignmentsByListId", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<AssignmentResponse>> getAssignmentsByListId(
            @RequestBody ListIdRequest request
    );
}
