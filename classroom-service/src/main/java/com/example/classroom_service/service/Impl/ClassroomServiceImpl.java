package com.example.classroom_service.service.Impl;

import com.example.classroom_service.dto.request.ClassroomCreateRequest;
import com.example.classroom_service.dto.request.ClassroomUpdateRequest;
import com.example.classroom_service.dto.response.ClassroomResponse;
import com.example.classroom_service.dto.response.SubjectWithClassroomResponse;
import com.example.classroom_service.entity.Classroom;
import com.example.classroom_service.mapper.ClassroomMapper;
import com.example.classroom_service.repository.ClassroomDetailRepository;
import com.example.classroom_service.repository.ClassroomRepository;
import com.example.classroom_service.service.ClassroomService;
import com.example.classroom_service.util.ClassroomUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassroomServiceImpl implements ClassroomService {
    ClassroomRepository classroomRepository;
    ClassroomMapper classroomMapper;
    ClassroomDetailRepository classroomDetailRepository;

    @Override
    public ClassroomResponse createClassroom(ClassroomCreateRequest request) {
        Classroom classroom = classroomMapper.toClassroom(request);
        String classCode;
        do {
            classCode = ClassroomUtil.generateCode(); // sinh mã 6 ký tự
        } while (classroomRepository.existsByClassCode(classCode));

        classroom.setClassCode(classCode);
        classroom.setCreatedAt(LocalDateTime.now());
        return classroomMapper.toClassroomResponse(classroomRepository.save(classroom));
    }

    @Override
    public List<ClassroomResponse> getAllClassrooms() {
        return classroomMapper.toClassroomResponses(classroomRepository.findAll());
    }

    @Override
    public ClassroomResponse getClassroomById(int classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + classroomId));
        return classroomMapper.toClassroomResponse(classroom);
    }

    @Override
    public ClassroomResponse updateClassroom(int classroomId, ClassroomUpdateRequest request) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + classroomId));
        classroomMapper.updateClassroom(classroom, request);
        classroomRepository.save(classroom);
        return classroomMapper.toClassroomResponse(classroom);
    }

    @Override
    public void deleteClassroom(int classroomId) {
        classroomRepository.deleteById(classroomId);
    }

    @Override
    public Page<ClassroomResponse> searchClassrooms(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Classroom> classrooms = classroomRepository.searchClassrooms(q, pageable);
        return classrooms.map(classroomMapper::toClassroomResponse);
    }

    @Override
    public Page<ClassroomResponse> findClassroomsByTeacherUsername(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Classroom> classrooms = classroomRepository.findByTeacherUsername(username, pageable);
        return classrooms.map(classroomMapper::toClassroomResponse);
    }

    @Override
    public List<SubjectWithClassroomResponse> getListSubjectsByClassrooms(List<Integer> listClassroomId){
        List<SubjectWithClassroomResponse> subjectResponses = new ArrayList<>();
        for(Integer classroomId:listClassroomId){
            Optional<Classroom> classroom = (classroomRepository.findById(classroomId));
            classroom.ifPresent(value -> subjectResponses.add(
                    new SubjectWithClassroomResponse(classroomId, classroom.get().getSubject().getId(), classroom.get().getSubject().getName()))
            );
        }
        return subjectResponses;
    }

    @Override
    public List<Integer> getListClass(){
        return classroomRepository.getAllClassroomId();
    }

}
