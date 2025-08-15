package org.example.scoreservice.repository.http;

import org.example.scoreservice.dto.response.ApiResponse;
import org.example.scoreservice.dto.response.SubjectWithClassroomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "classroom-service",url = "${app.services.classrooms}")
public interface ClassroomClient {
    @PostMapping("/get-list-subjects-by-classrooms")
    ApiResponse<List<SubjectWithClassroomResponse>> getListSubjectByClassrooms(
            @RequestBody List<Integer> listClassroomId
    );

    @PostMapping("/get-list-class")
    ApiResponse<List<Integer>> getListClass();
}
