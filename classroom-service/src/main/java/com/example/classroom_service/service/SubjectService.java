package com.example.classroom_service.service;

import com.example.classroom_service.dto.request.SubjectCreateRequest;
import com.example.classroom_service.dto.request.SubjectUpdateRequest;
import com.example.classroom_service.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {
    SubjectResponse createSubject(SubjectCreateRequest request);
    SubjectResponse getSubjectById(int subjectId);
    List<SubjectResponse> getAllSubjects();
    SubjectResponse updateSubject(int subjectId, SubjectUpdateRequest request);
    void deleteSubject(int subjectId);

    List<SubjectResponse> getListSubjectsById(List<Integer> listSubjectId);
}
