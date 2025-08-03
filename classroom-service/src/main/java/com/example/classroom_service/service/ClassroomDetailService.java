package com.example.classroom_service.service;

import com.example.classroom_service.dto.request.StudentAddRequest;
import com.example.classroom_service.dto.request.StudentRemoveRequest;
import com.example.classroom_service.dto.response.StudentResponse;
import com.example.classroom_service.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassroomDetailService {
    StudentResponse addStudent(StudentAddRequest request);
    void deleteStudent(StudentRemoveRequest request);
    Page<StudentResponse> findStudentClasses(String studentUsername, int page, int size);
    Page<UserProfileResponse> getStudentsOfClass(int classroomId, int page, int size);
    List<String> findStudentUsernamesByClassroomId(int classroomId);
}
