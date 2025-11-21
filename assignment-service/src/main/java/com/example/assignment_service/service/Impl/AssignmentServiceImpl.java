package com.example.assignment_service.service.Impl;

import com.example.assignment_service.dto.request.*;
import com.example.assignment_service.dto.response.*;
import com.example.assignment_service.entity.Assignment;
import com.example.assignment_service.entity.AssignmentDetail;
import com.example.assignment_service.enums.AssignmentDetailStatus;
import com.example.assignment_service.enums.AssignmentSubmitStatus;
import com.example.assignment_service.event.AuditLogEvent;
import com.example.assignment_service.mapper.AssignmentDetailMapper;
import com.example.assignment_service.mapper.AssignmentMapper;
import com.example.assignment_service.repository.AssignmentDetailRepository;
import com.example.assignment_service.repository.AssignmentRepository;
import com.example.assignment_service.repository.httpclient.ClassroomClient;
import com.example.assignment_service.repository.httpclient.ProfileClient;
import com.example.assignment_service.service.AssignmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentServiceImpl implements AssignmentService {
    AssignmentRepository assignmentRepository;
    AssignmentMapper assignmentMapper;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");
    AssignmentDetailRepository assignmentDetailRepository;
    AssignmentDetailMapper assignmentDetailMapper;
    ClassroomClient classroomClient;
    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public AssignmentResponse createAssignment(AssignmentCreateRequest request) {
//        LocalDateTime deadline;

        String[] parts = request.getDeadline().split(" ");
        LocalTime time = LocalTime.parse(parts[0], timeFormatter);
        LocalDate date = LocalDate.parse(parts[1], dateFormatter);

        LocalDateTime dateTime = LocalDateTime.of(date, time);

//        Assignment assignment = assignmentMapper.toAssignment(request);
        Assignment assignment = new Assignment();
        assignment.setAssignmentCode(request.getUsername() + "_assignment_" + request.getDeadline());
        assignment.setDeadline(dateTime);
        assignment.setFileUrl(request.getFileUrl());
        assignment.setName(request.getName());
        assignment.setUsername(request.getUsername());
        assignment.setClassroomId(request.getClassroomId());
        assignment = assignmentRepository.save(assignment);
        AuditLogEvent logEvent = new AuditLogEvent(
                request.getUsername(),
                "TEACHER",
                "CREATE ASSIGNMENT",
                "Created new assignment with ID: " + assignment.getId() + " for classroom with ID: " + request.getClassroomId()
        );
        kafkaTemplate.send("audit.log", logEvent);
        return assignmentMapper.toAssignmentResponse(assignment);
    }

    @Override
    public List<AssignmentResponse> getAllAssignments() {
        return assignmentMapper.toAssignmentResponses(assignmentRepository.findAll());
    }

    @Override
    public AssignmentResponse getAssignmentById(int assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));
        return assignmentMapper.toAssignmentResponse(assignment);
    }

    @Override
    public AssignmentResponse updateAssignment(int assignmentId, AssignmentUpdateRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));

        if (!assignment.getDeadline().equals(request.getDeadline()) ||
                !assignment.getUsername().equals(request.getUsername())) {
            assignment.setAssignmentCode(request.getUsername() + "_assignment_" + request.getDeadline());
        }
        assignmentMapper.updateAssignment(assignment, request);
        assignmentRepository.save(assignment);
        AuditLogEvent logEvent = new AuditLogEvent(
                request.getUsername(),
                "TEACHER",
                "UPDATE ASSIGNMENT",
                "Updated assignment with ID: " + assignmentId + ". request: " + request.toString()
        );
        kafkaTemplate.send("audit.log", logEvent);
        return assignmentMapper.toAssignmentResponse(assignment);
    }

    @Override
    public void deleteAssignment(int assignmentId) throws GeneralSecurityException, IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));
        assignmentRepository.deleteById(assignmentId);
    }

    @Override
    public AssignmentDetailResponse submitAssignment(AssignmentSubmitRequest request) {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found with ID: " + request.getAssignmentId()));

        AssignmentDetail detail = assignmentDetailRepository
                .findByAssignment_IdAndStudentUsername(request.getAssignmentId(), request.getStudentUsername())
                .orElse(null);

        if (detail == null) {
            detail = AssignmentDetail.builder()
                    .assignment(assignment)
                    .studentUsername(request.getStudentUsername())
                    .note(request.getNote())
                    .fileUrl(request.getFileUrl())
                    .submitTime(LocalDateTime.now())
                    .submissionCount(1)
                    .build();
        } else {
            // update: tăng counter, cập nhật file/note/time
            detail.setSubmitTime(LocalDateTime.now());
            detail.setNote(request.getNote());
            detail.setFileUrl(request.getFileUrl());
            detail.setSubmissionCount((detail.getSubmissionCount() == null ? 0 : detail.getSubmissionCount()) + 1);
        }

        detail = assignmentDetailRepository.save(detail);

        return assignmentDetailMapper.toAssignmentDetailResponse(detail, assignment.getDeadline());
    }

    @Override
    public Page<StudentAssignmentViewResponse> getSubmissionsByAssignment(Integer assignmentId, String status, Pageable pageable) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        List<String> allStudents = classroomClient
                .findStudentUsernamesByClassroomId(assignment.getClassroomId()).getResult();

        List<AssignmentDetail> details = assignmentDetailRepository.findByAssignment_Id(assignmentId);

        Map<String, AssignmentDetail> detailMap = details.stream()
                .collect(Collectors.toMap(AssignmentDetail::getStudentUsername, Function.identity()));

        List<StudentAssignmentViewResponse> responses = new ArrayList<>();

        for (String username : allStudents) {
            AssignmentDetail det = detailMap.get(username);

            AssignmentSubmitStatus overallStatus;
            AssignmentDetailResponse latestSubmission = null;

            if (det == null) {
                responses.add(StudentAssignmentViewResponse.builder()
                        .studentUsername(username)
                        .status(AssignmentSubmitStatus.NOT_SUBMITTED)
                        .submission(null)
                        .build());
            } else {
                AssignmentDetailResponse subResp = assignmentDetailMapper.toAssignmentDetailResponse(det, assignment.getDeadline());

                AssignmentSubmitStatus overall = (subResp.getStatus() == AssignmentDetailStatus.LATE)
                        ? AssignmentSubmitStatus.LATE
                        : AssignmentSubmitStatus.SUBMITTED;

                responses.add(StudentAssignmentViewResponse.builder()
                        .studentUsername(username)
                        .status(overall)
                        .submission(subResp)
                        .build());
            }
        }

        // Lọc theo trạng thái tổng thể nếu có
        if (status != null && !status.isBlank()) {
            AssignmentSubmitStatus desired = AssignmentSubmitStatus.valueOf(status.toUpperCase());
            responses = responses.stream()
                    .filter(r -> r.getStatus() == desired)
                    .collect(Collectors.toList());
        }

        responses = enrichWithProfileInfo(responses);

        // Phân trang
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), responses.size());
        List<StudentAssignmentViewResponse> pageContent = responses.subList(start, end);

        return new PageImpl<>(pageContent, pageable, responses.size());
    }

    private List<StudentAssignmentViewResponse> enrichWithProfileInfo(List<StudentAssignmentViewResponse> responses) {
        if (responses == null || responses.isEmpty()) return responses;

        List<String> usernames = responses.stream()
                .map(StudentAssignmentViewResponse::getStudentUsername)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<UserProfileResponse> userProfiles = profileClient.getListUserByListUsername(new ListUsernameRequest(usernames)).getResult();

        Map<String, UserProfileResponse> profileMap = userProfiles.stream()
                .collect(Collectors.toMap(UserProfileResponse::getUsername, p -> p));

        responses.forEach(r -> {
            UserProfileResponse profile = profileMap.get(r.getStudentUsername());
            if (profile != null) {
                r.setStudentName(profile.getFullName());
                r.setAvatar(profile.getAvatar());
            } else {
                r.setStudentName("Unknown");
                r.setAvatar(null);
            }
        });

        return responses;
    }

    //    @Override
//    public boolean checkSubmission(Integer assignmentId, String studentUsername) {
//        return assignmentDetailRepository.existsByAssignment_IdAndStudentUsername(assignmentId, studentUsername);
//    }
//
    @Override
    public AssignmentDetailResponse getSubmissionOfStudent(Integer assignmentId, String studentUsername) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        AssignmentDetail assignmentDetail = assignmentDetailRepository.findByAssignment_IdAndStudentUsername(assignmentId, studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not submit yet"));
        return assignmentDetailMapper.toAssignmentDetailResponse(assignmentDetail, assignment.getDeadline());
    }

    //
    @Override
    public List<AssignmentResponse> getAssignmentsByIds(ListIdRequest request) {
        return getAssignmentResponsesFromIds(request.getIdsList());
    }

    @Override
    public Page<AssignmentViewForStudent> getAssignmentsForStudent(String studentUsername, Integer classroomId, String status, Pageable pageable) {
        List<String> studentsInClass = Optional.ofNullable(classroomClient
                        .findStudentUsernamesByClassroomId(classroomId).getResult())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve students for classroom"));
        if (!studentsInClass.contains(studentUsername)) {
            throw new RuntimeException("Student is not enrolled in this classroom");
        }

        List<Assignment> assignments = assignmentRepository.findByClassroomId(classroomId);
        List<AssignmentDetail> studentDetails = assignmentDetailRepository.findByStudentUsername(studentUsername);

        Map<Integer, AssignmentDetail> detailMap = studentDetails.stream()
                .collect(Collectors.toMap(detail -> detail.getAssignment().getId(), Function.identity()));

        List<AssignmentViewForStudent> allViews = assignments.stream()
                .map(assignment -> {
                    AssignmentDetail detail = detailMap.get(assignment.getId());
                    AssignmentResponse assignmentResponse = assignmentMapper.toAssignmentResponse(assignment);
                    AssignmentViewForStudent view = AssignmentViewForStudent.builder()
                            .assignment(assignmentResponse)
                            .build();

                    if (detail != null) {
                        AssignmentDetailResponse detailResponse = assignmentDetailMapper.toAssignmentDetailResponse(detail, assignment.getDeadline());
                        view.setSubmission(detailResponse);
                        view.setStatus(detailResponse.getStatus() == AssignmentDetailStatus.LATE ? AssignmentSubmitStatus.LATE : AssignmentSubmitStatus.SUBMITTED);
                    } else {
                        view.setStatus(AssignmentSubmitStatus.NOT_SUBMITTED);
                        view.setSubmission(null);
                    }
                    return view;
                })
                .filter(view -> status == null || view.getStatus().name().equalsIgnoreCase(status))
                .collect(Collectors.toList());

        // Sort and paginate (assuming pageable has sort by deadline desc as example)
        if (pageable.getSort().isSorted()) {
            // Implement sort based on Sort object, e.g., by deadline
            allViews.sort(Comparator.comparing(v -> v.getAssignment().getDeadline(), Comparator.reverseOrder()));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allViews.size());
        List<AssignmentViewForStudent> pageContent = allViews.subList(start, end);

        return new PageImpl<>(pageContent, pageable, allViews.size());
    }

    public List<AssignmentResponse> getAssignmentResponsesFromIds(List<Integer> assignmentIds) {
        List<Integer> validIds = assignmentIds.stream()
                .filter(id -> id > 0)
                .collect(Collectors.toList());

        List<Assignment> assignments = assignmentRepository.findByIdIn(validIds);

        Map<Integer, Assignment> assignmentMap = assignments.stream()
                .collect(Collectors.toMap(Assignment::getId, a -> a));

        return assignmentIds.stream()
                .map(id -> {
                    if (id == 0) {
                        return null;
                    }
                    Assignment assignment = assignmentMap.get(id);
                    return assignment != null ? assignmentMapper.toAssignmentResponse(assignment) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}