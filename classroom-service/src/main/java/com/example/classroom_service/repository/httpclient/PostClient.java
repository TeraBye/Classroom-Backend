package com.example.classroom_service.repository.httpclient;

import com.example.classroom_service.dto.request.ListUsernameRequest;
import com.example.classroom_service.dto.response.ApiResponse;
import com.example.classroom_service.dto.response.ClassPostResponse;
import com.example.classroom_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "post-service", url = "${app.services.post}")
public interface PostClient {
    @GetMapping(value = "/count-by-classrooms", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<ClassPostResponse>> getPostNumByClassIds(@RequestParam List<Integer> classIds);
}
