package com.example.classroom_service.service.Impl;

import com.example.classroom_service.dto.request.SubjectCreateRequest;
import com.example.classroom_service.dto.request.SubjectUpdateRequest;
import com.example.classroom_service.dto.response.SubjectResponse;
import com.example.classroom_service.entity.Subject;
import com.example.classroom_service.mapper.SubjectMapper;
import com.example.classroom_service.repository.SubjectRepository;
import com.example.classroom_service.service.SubjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    SubjectMapper subjectMapper;
    @Override
    public SubjectResponse createSubject(SubjectCreateRequest request) {
        Subject subject = subjectMapper.toSubject(request);
        return subjectMapper.toSubjectResponse(subjectRepository.save(subject));
    }

    @Override
    public SubjectResponse getSubjectById(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectId));
        return subjectMapper.toSubjectResponse(subject);
    }

    @Override
    public List<SubjectResponse> getAllSubjects() {
        return subjectMapper.toSubjectResponses(subjectRepository.findAll());
    }

    @Override
    public SubjectResponse updateSubject(int subjectId, SubjectUpdateRequest request) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectId));

        subjectMapper.updateSubject(subject, request);

        subjectRepository.save(subject);
        return subjectMapper.toSubjectResponse(subject);
    }

    @Override
    public void deleteSubject(int subjectId) {
        subjectRepository.deleteById(subjectId);
    }

    @Override
    public List<SubjectResponse> getListSubjectsById(List<Integer> listSubjectId){
        List<SubjectResponse> subjectResponses = new ArrayList<>();
        for(Integer subjectId:listSubjectId){
            Optional<Subject> subject = (subjectRepository.findById(subjectId));
            subject.ifPresent(value -> subjectResponses.add(new SubjectResponse(subjectId, value.getName())));
        }
        return subjectResponses;
    }
}
