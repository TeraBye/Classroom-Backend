package com.example.question_service.repository.http;

import com.example.question_service.dto.response.ApiResponse;
import com.example.question_service.dto.response.SubjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "classroom-service",url = "${app.services.classrooms}")
public interface ClassroomClient {
    @PostMapping("/subjects/get-list-subjects")
    ApiResponse<List<SubjectResponse>> getListSubject(
            @RequestBody List<Integer> listSubjectId
    );
}
