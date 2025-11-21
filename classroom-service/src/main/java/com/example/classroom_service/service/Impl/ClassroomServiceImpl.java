package com.example.classroom_service.service.Impl;

import com.example.classroom_service.dto.request.ClassroomCreateRequest;
import com.example.classroom_service.dto.request.ClassroomUpdateRequest;
import com.example.classroom_service.dto.request.ListUsernameRequest;
import com.example.classroom_service.dto.response.*;
import com.example.classroom_service.entity.Classroom;
import com.example.classroom_service.entity.ClassroomDetail;
import com.example.classroom_service.entity.Subject;
import com.example.classroom_service.entity.TeacherSubject;
import com.example.classroom_service.event.AuditLogEvent;
import com.example.classroom_service.mapper.ClassroomDetailMapper;
import com.example.classroom_service.mapper.ClassroomMapper;
import com.example.classroom_service.repository.ClassroomDetailRepository;
import com.example.classroom_service.repository.ClassroomRepository;
import com.example.classroom_service.repository.SubjectRepository;
import com.example.classroom_service.repository.TeacherSubjectRepository;
import com.example.classroom_service.repository.httpclient.PostClient;
import com.example.classroom_service.repository.httpclient.ProfileClient;
import com.example.classroom_service.service.ClassroomService;
import com.example.classroom_service.util.ClassroomUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassroomServiceImpl implements ClassroomService {
    ClassroomRepository classroomRepository;
    ClassroomMapper classroomMapper;
    SubjectRepository subjectRepository;
    TeacherSubjectRepository teacherSubjectRepository;
    ClassroomDetailRepository classroomDetailRepository;
    ClassroomDetailMapper classroomDetailMapper;
    PostClient postClient;
    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    private void fillPostNums(List<ClassroomResponse> responses) {
        if (responses.isEmpty()) return;

        List<Integer> classIds = responses.stream().map(ClassroomResponse::getId).toList();
        List<ClassPostResponse> postCounts = postClient.getPostNumByClassIds(classIds).getResult();

        Map<Integer, Long> postMap = postCounts.stream()
                .collect(Collectors.toMap(ClassPostResponse::getClassId, ClassPostResponse::getPostNum));

        responses.forEach(r -> r.setPostNum(postMap.getOrDefault(r.getId(), 0L)));
    }

    private void fillTeacherNames(List<ClassroomResponse> responses) {
        if (responses.isEmpty()) return;

        List<String> teacherUsernames = responses.stream()
                .map(ClassroomResponse::getTeacherUsername)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (teacherUsernames.isEmpty()) return;

        List<UserProfileResponse> teacherProfiles =
                profileClient.getListUserByListUsername(new ListUsernameRequest(teacherUsernames)).getResult();

        Map<String, String> teacherMap = teacherProfiles.stream()
                .collect(Collectors.toMap(UserProfileResponse::getUsername, UserProfileResponse::getFullName));

        responses.forEach(r -> r.setTeacherName(teacherMap.get(r.getTeacherUsername())));
    }


    @Override
    public ClassroomResponse createClassroom(ClassroomCreateRequest request) {
        if (classroomRepository.existsByName(request.getName())) {
            throw new RuntimeException("Class name existed");
        }
        TeacherSubject teacherSubject = findOrCreateTeacherSubject(request.getTeacherUsername(), request.getSubjectId());
        Classroom classroom = classroomMapper.toClassroom(request);
        String classCode;
        do {
            classCode = ClassroomUtil.generateCode(); // sinh mã 6 ký tự
        } while (classroomRepository.existsByClassCode(classCode));

        classroom.setTeacherSubject(teacherSubject);
        classroom.setClassCode(classCode);
        classroom.setCreatedAt(LocalDateTime.now());

        ClassroomResponse response = classroomMapper.toClassroomResponse(classroomRepository.save(classroom));
        fillTeacherNames(List.of(response));
        AuditLogEvent logEvent = new AuditLogEvent(
                request.getTeacherUsername(),
                "TEACHER",
                "CREATE CLASSROOM",
                "Created new classroom with ID: " + response.getId() + " for subject with ID: " + request.getSubjectId()
        );
        kafkaTemplate.send("audit.log", logEvent);
        return response;
    }

    private TeacherSubject findOrCreateTeacherSubject(String teacherUsername, Integer subjectId) {
        return teacherSubjectRepository.findByTeacherUsernameAndSubject_Id(teacherUsername, subjectId)
                .orElseGet(() -> {
                    Subject subject = subjectRepository.findById(subjectId)
                            .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

                    TeacherSubject newTeacherSubject = TeacherSubject.builder()
                            .teacherUsername(teacherUsername)
                            .subject(subject)
                            .build();

                    return teacherSubjectRepository.save(newTeacherSubject);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassroomResponse> getAllClassrooms() {
        List<ClassroomResponse> responses = classroomMapper.toClassroomResponses(classroomRepository.findAll());
        fillTeacherNames(responses);
        fillPostNums(responses);
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public ClassroomResponse getClassroomById(int classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + classroomId));
        ClassroomResponse response = classroomMapper.toClassroomResponse(classroom);
        fillPostNums(List.of(response));
        return response;
    }

    @Override
    public ClassroomResponse updateClassroom(int classroomId, ClassroomUpdateRequest request) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + classroomId));

        if (!classroom.getName().equals(request.getName())
                && classroomRepository.existsByName(request.getName())) {
            throw new RuntimeException("Class name existed");
        }

        classroomMapper.updateClassroom(classroom, request);
        classroom.setPublic(request.isPublic());
        classroomRepository.save(classroom);
        ClassroomResponse response = classroomMapper.toClassroomResponse(classroom);
        fillTeacherNames(List.of(response));
        fillPostNums(List.of(response));

        AuditLogEvent logEvent = new AuditLogEvent(
                classroom.getTeacherSubject().getTeacherUsername(),
                "TEACHER",
                "UPDATE CLASSROOM",
                "Updated classroom with ID: " + classroomId + ". Request: " + request.toString()
        );
        kafkaTemplate.send("audit.log", logEvent);
        return response;
    }

    @Override
    public void deleteClassroom(int classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + classroomId));
        classroom.setDeleted(true);
        classroomRepository.save(classroom);

        AuditLogEvent logEvent = new AuditLogEvent(
                classroom.getTeacherSubject().getTeacherUsername(),
                "TEACHER",
                "DELETE CLASSROOM",
                "Deleted classroom with ID: " + classroomId
        );
        kafkaTemplate.send("audit.log", logEvent);
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
//        Page<Classroom> classrooms = classroomRepository.findByTeacherSubject_TeacherUsernameAndIsDeleted(username, false, pageable);
        Page<Classroom> classrooms = classroomRepository.findByTeacherSubject_TeacherUsername(username, pageable);

        List<ClassroomResponse> responses = classrooms.getContent()
                .stream()
                .map(classroomMapper::toClassroomResponse)
                .collect(Collectors.toList());

        fillTeacherNames(responses);
        fillPostNums(responses);
        return new PageImpl<>(responses, pageable, classrooms.getTotalElements());
    }

    @Override
    public Page<StudentResponse> findStudentClasses(String studentUsername, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassroomDetail> classroomDetails = classroomDetailRepository.findByStudentUsername(studentUsername, pageable);

        List<StudentResponse> studentResponses = classroomDetails.stream()
                .map(classroomDetailMapper::toStudentResponse)
                .toList();

        List<ClassroomResponse> classroomResponses = studentResponses.stream()
                .map(StudentResponse::getClassroom)
                .filter(Objects::nonNull)
                .toList();

        fillTeacherNames(classroomResponses);
        fillPostNums(classroomResponses);
        return new PageImpl<>(studentResponses, pageable, classroomDetails.getTotalElements());
    }

    @Override
    public void restore(int classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with ID: " + classroomId));

        classroom.setDeleted(false);
        classroomRepository.save(classroom);

        AuditLogEvent logEvent = new AuditLogEvent(
                classroom.getTeacherSubject().getTeacherUsername(),
                "TEACHER",
                "RESTORE CLASSROOM",
                "Restored classroom with ID: " + classroomId
        );
        kafkaTemplate.send("audit.log", logEvent);
    }

    @Override
    public List<SubjectWithClassroomResponse> getListSubjectsByClassrooms(List<Integer> listClassroomId) {
        List<SubjectWithClassroomResponse> subjectResponses = new ArrayList<>();
        for (Integer classroomId : listClassroomId) {
            Optional<Classroom> classroom = (classroomRepository.findById(classroomId));
//            classroom.ifPresent(value -> subjectResponses.add(
//                    new SubjectWithClassroomResponse(classroomId, classroom.get().getSubject().getId(), classroom.get().getSubject().getName()))
//            );
        }
        return subjectResponses;
    }

    @Override
    public List<Integer> getListClass() {
        return classroomRepository.getAllClassroomId();
    }

}
