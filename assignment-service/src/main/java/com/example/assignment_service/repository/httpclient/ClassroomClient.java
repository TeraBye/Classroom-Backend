package com.example.assignment_service.repository.httpclient;

import com.example.assignment_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "classroom-service", url = "${app.services.classroom}")
public interface ClassroomClient {
    @GetMapping(value = "/get-student-usernames/{classroomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<String>> findStudentUsernamesByClassroomId(@PathVariable int classroomId);
}
