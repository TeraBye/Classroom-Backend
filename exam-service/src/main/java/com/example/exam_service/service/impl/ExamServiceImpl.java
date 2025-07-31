package com.example.exam_service.service.impl;

import com.example.exam_service.dto.request.ExamCreationRequest;
import com.example.exam_service.dto.response.ExamResponse;
import com.example.exam_service.entity.Exam;
import com.example.exam_service.mapper.ExamMapper;
import com.example.exam_service.repository.ExamRepository;
import com.example.exam_service.service.ExamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExamServiceImpl implements ExamService {
    ExamRepository examRepository;
    ExamMapper examMapper;

    @Override
    public ExamResponse createExam(ExamCreationRequest request){
        Exam exam = examMapper.toExam(request);
        return examMapper.toCoExamResponse(examRepository.save(exam));
    }

}
