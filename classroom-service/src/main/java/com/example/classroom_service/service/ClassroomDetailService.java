package com.example.classroom_service.service;

import com.example.classroom_service.dto.request.StudentAddRequest;
import com.example.classroom_service.dto.request.StudentRemoveRequest;
import com.example.classroom_service.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

public interface ClassroomDetailService {
    StudentResponse addStudent(StudentAddRequest request);
    void deleteStudent(StudentRemoveRequest request);
    Page<StudentResponse> findStudentClasses(String studentUsername, int page, int size);
}
