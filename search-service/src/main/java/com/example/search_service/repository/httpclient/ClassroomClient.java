package com.example.search_service.repository.httpclient;

import com.example.search_service.dto.response.ApiResponse;
import com.example.search_service.dto.response.ClassroomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "classroom-service", url = "${app.services.classroom}")
public interface ClassroomClient {
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Page<ClassroomResponse>> searchClassrooms(
            @RequestParam("q") String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size);
}
