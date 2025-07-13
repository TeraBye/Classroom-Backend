package com.example.classroom_service.service.Impl;

import com.example.classroom_service.dto.request.StudentAddRequest;
import com.example.classroom_service.dto.request.StudentRemoveRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Classroom classroom = classroomRepository.findByClassCode(request.getClassCode().trim())
                .orElseThrow(() -> new RuntimeException("Classroom not found with class code: " + request.getClassCode()));

        // Check if the student is already enrolled in the classroom
        boolean studentExists = classroomDetailRepository.existsByClassroomAndStudentUsername(
                classroom, request.getStudentUsername().trim());
        if (studentExists) {
            throw new RuntimeException("Student with username " + request.getStudentUsername() + " is already enrolled in the classroom.");
        }

        ClassroomDetail classroomDetail = ClassroomDetail.builder()
                .classroom(classroom)
                .studentUsername(request.getStudentUsername())
                .joinedAt(LocalDateTime.now())
                .build();

        return classroomDetailMapper.toStudentResponse(classroomDetailRepository.save(classroomDetail));
    }

    @Override
    public void deleteStudent(StudentRemoveRequest request) {
        Classroom classroom = classroomRepository.findByClassCode(request.getClassCode().trim())
                .orElseThrow(() -> new RuntimeException("Classroom not found with class code: " + request.getClassCode().trim()));

        ClassroomDetail classroomDetail = classroomDetailRepository.findByClassroomAndStudentUsername(classroom,
                        request.getStudentUsername().trim())
                .orElseThrow(() -> new RuntimeException("Student with username " + request.getStudentUsername() + " is not enrolled in the classroom."));

        classroomDetailRepository.delete(classroomDetail);
    }

    @Override
    public Page<StudentResponse> findStudentClasses(String studentUsername, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassroomDetail> classroomDetails = classroomDetailRepository.findByStudentUsername(studentUsername, pageable);
        return classroomDetails.map(classroomDetailMapper::toStudentResponse);
    }
}
