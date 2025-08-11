package org.example.scoreservice.repository.http;

import org.example.scoreservice.dto.response.ApiResponse;
import org.example.scoreservice.dto.response.SubjectResponse;
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
