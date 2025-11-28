package com.example.classroom_service.service.Impl;

import com.example.classroom_service.dto.request.ListUsernameRequest;
import com.example.classroom_service.dto.request.StudentAddRequest;
import com.example.classroom_service.dto.request.StudentRemoveRequest;
import com.example.classroom_service.dto.response.StudentResponse;
import com.example.classroom_service.dto.response.UserProfileResponse;
import com.example.classroom_service.entity.Classroom;
import com.example.classroom_service.entity.ClassroomDetail;
import com.example.classroom_service.entity.JoinRequest;
import com.example.classroom_service.enums.JoinStatus;
import com.example.classroom_service.mapper.ClassroomDetailMapper;
import com.example.classroom_service.mapper.ClassroomMapper;
import com.example.classroom_service.repository.ClassroomDetailRepository;
import com.example.classroom_service.repository.ClassroomRepository;
import com.example.classroom_service.repository.JoinRequestRepository;
import com.example.classroom_service.repository.httpclient.ProfileClient;
import com.example.classroom_service.service.ClassroomDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassroomDetailServiceImpl implements ClassroomDetailService {
    ClassroomRepository classroomRepository;
    ClassroomDetailRepository classroomDetailRepository;
    ClassroomDetailMapper classroomDetailMapper;
    ProfileClient profileClient;
    ClassroomMapper classroomMapper;
    JoinRequestRepository joinRequestRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

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

        if (classroom.getIsPublic()) {
            ClassroomDetail classroomDetail = ClassroomDetail.builder()
                    .classroom(classroom)
                    .studentUsername(request.getStudentUsername())
                    .joinedAt(LocalDateTime.now())
                    .build();
            return classroomDetailMapper.toStudentResponse(classroomDetailRepository.save(classroomDetail));
        } else {
            Optional<JoinRequest> existingRequest = joinRequestRepository.findByClassroom_IdAndStudentUsername(classroom.getId(), request.getStudentUsername());
            if (existingRequest.isPresent() && JoinStatus.PENDING.name().equals(existingRequest.get().getStatus())) {
                throw new RuntimeException("You have already sent a join request for this classroom.");
            }

            JoinRequest joinRequest = JoinRequest.builder()
                    .classroom(classroom)
                    .studentUsername(request.getStudentUsername())
                    .teacherUsername(classroom.getTeacherSubject().getTeacherUsername())
                    .status(JoinStatus.PENDING.name())
                    .requestedAt(LocalDateTime.now())
                    .build();
            joinRequestRepository.save(joinRequest);

            Map<String, Object> notiPayload = new HashMap<>();
            notiPayload.put("type", "join_request");
            notiPayload.put("content", request.getStudentUsername() + " requested to join: " + classroom.getName());
            notiPayload.put("senderUsername", request.getStudentUsername());
            notiPayload.put("receiverUsername", classroom.getTeacherSubject().getTeacherUsername());
            notiPayload.put("classroomId", classroom.getId().toString());
            notiPayload.put("requestId", joinRequest.getId().toString());

            kafkaTemplate.send("notifications", notiPayload);

            throw new RuntimeException("Join request sent successfully. Waiting for teacher approval.");
        }

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
    public Page<UserProfileResponse> getStudentsOfClass(int classroomId, int page, int size) {
        // Validate input
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be non-negative and size must be positive");
        }

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paged usernames from repository
        Page<String> usernamePage = classroomDetailRepository.findStudentUsernamesByClassroomIdPagination(classroomId, pageable);

        // Check if usernames are empty
        if (usernamePage.getContent().isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // Fetch user profiles from profileClient
        List<UserProfileResponse> profiles;
        try {
            profiles = profileClient
                    .getListUserByListUsername(new ListUsernameRequest(usernamePage.getContent()))
                    .getResult();
        } catch (Exception e) {
            // handle appropriately
            throw new RuntimeException("Error fetching user profiles " + e.getMessage());
        }

        // Ensure profiles are not null
        profiles = (profiles != null) ? profiles : Collections.emptyList();

        // Return paged result
        return new PageImpl<>(profiles, pageable, usernamePage.getTotalElements());
    }

    @Override
    public List<String> findStudentUsernamesByClassroomId(int classroomId) {
        return classroomDetailRepository.findStudentUsernamesByClassroomId(classroomId);
    }
}
