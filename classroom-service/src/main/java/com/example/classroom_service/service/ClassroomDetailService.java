package com.example.classroom_service.service;

import com.example.classroom_service.dto.request.StudentAddRequest;
import com.example.classroom_service.dto.request.StudentRemoveRequest;
import com.example.classroom_service.dto.response.StudentResponse;

public interface ClassroomDetailService {
    StudentResponse addStudent(StudentAddRequest request);
    void deleteStudent(StudentRemoveRequest request);
}
