package com.example.classroom_service.service.Impl;

import com.example.classroom_service.dto.request.StudentAddRequest;
import com.example.classroom_service.dto.response.StudentResponse;
import com.example.classroom_service.entity.Classroom;
import com.example.classroom_service.entity.ClassroomDetail;
import com.example.classroom_service.mapper.ClassroomDetailMapper;
import com.example.classroom_service.repository.ClassroomDetailRepository;
import com.example.classroom_service.repository.ClassroomRepository;
import com.example.classroom_service.service.ClassroomDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassroomDetailServiceImpl implements ClassroomDetailService {
    ClassroomRepository classroomRepository;
    ClassroomDetailRepository classroomDetailRepository;
    ClassroomDetailMapper classroomDetailMapper;
    @Override
    public StudentResponse addStudent(StudentAddRequest request) {
        Classroom classroom = classroomRepository.findByClassCode(request.getClassCode())
                .orElseThrow(() -> new RuntimeException("Classroom not found with class code: " + request.getClassCode()));

        ClassroomDetail classroomDetail = ClassroomDetail.builder()
                .classroom(classroom)
                .studentUsername(request.getStudentUsername())
                .joinedAt(LocalDateTime.now())
                .build();

        return classroomDetailMapper.toAddStudentResponse(classroomDetailRepository.save(classroomDetail));
    }
}
