package com.example.classroom_service.service;

import com.example.classroom_service.dto.request.ClassroomCreateRequest;
import com.example.classroom_service.dto.request.ClassroomUpdateRequest;
import com.example.classroom_service.dto.response.ClassroomResponse;
import com.example.classroom_service.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassroomService {
    ClassroomResponse createClassroom(ClassroomCreateRequest request);
    List<ClassroomResponse> getAllClassrooms();
    ClassroomResponse getClassroomById(int classroomId);
    ClassroomResponse updateClassroom(int classroomId, ClassroomUpdateRequest request);
    void deleteClassroom(int classroomId);
    Page<ClassroomResponse> searchClassrooms(String q, int page, int size);
    Page<ClassroomResponse> findClassroomsByTeacherUsername(String username, int page, int size);

}
