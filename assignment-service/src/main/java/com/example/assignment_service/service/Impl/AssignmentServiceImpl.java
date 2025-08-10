package com.example.assignment_service.service.Impl;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentSubmitRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.AssignmentDetailResponse;
import com.example.assignment_service.dto.request.ListIdRequest;
import com.example.assignment_service.dto.response.AssignmentResponse;
import com.example.assignment_service.entity.Assignment;
import com.example.assignment_service.entity.AssignmentDetail;
import com.example.assignment_service.mapper.AssignmentDetailMapper;
import com.example.assignment_service.mapper.AssignmentMapper;
import com.example.assignment_service.repository.AssignmentDetailRepository;
import com.example.assignment_service.repository.AssignmentRepository;
import com.example.assignment_service.service.AssignmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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

    @Override
    public AssignmentResponse createAssignment(AssignmentCreateRequest request) throws GeneralSecurityException, IOException {
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
    public AssignmentResponse updateAssignment(int assignmentId, AssignmentUpdateRequest request) throws GeneralSecurityException, IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));

//        // Handle file upload if a new file is provided
//        String newFileUrl = assignment.getFileUrl();
//        if (request.getFile() != null && !request.getFile().isEmpty()) {
//            // Delete the old file if it exists
//            if (assignment.getFileUrl() != null) {
//                fileStorageService.deleteFile(assignment.getFileUrl());
//            }
//            // Upload the new file
//            newFileUrl = fileStorageService.uploadFile(request.getFile(), request.getUsername(), Optional.of(assignmentId), "TEACHER");
//        }
//
//        // Parse the deadline if provided
//        LocalDateTime deadline = assignment.getDeadline();
//        if (request.getDeadline() != null && !request.getDeadline().isBlank()) {
//            String[] parts = request.getDeadline().split(" ");
//            LocalTime time = LocalTime.parse(parts[0], timeFormatter);
//            LocalDate date = LocalDate.parse(parts[1], dateFormatter);
//            deadline = LocalDateTime.of(date, time);
//        }
//
//        // Update assignment code if username or deadline changed
//        if (request.getUsername() != null && !request.getUsername().isBlank() &&
//                request.getDeadline() != null && !request.getDeadline().isBlank() &&
//                (!assignment.getUsername().equals(request.getUsername()) || !assignment.getDeadline().equals(deadline))) {
//            assignment.setAssignmentCode(request.getUsername() + "_assignment_" + request.getDeadline());
//        }

        if (!assignment.getDeadline().equals(request.getDeadline()) ||
                !assignment.getUsername().equals(request.getUsername())) {
            assignment.setAssignmentCode(request.getUsername() + "_assignment_" + request.getDeadline());
        }
        assignmentMapper.updateAssignment(assignment, request);
//        assignment.setFileUrl(newFileUrl);
//        assignment.setDeadline(deadline);
        assignmentRepository.save(assignment);
        return assignmentMapper.toAssignmentResponse(assignment);
    }

    @Override
    public void deleteAssignment(int assignmentId) throws GeneralSecurityException, IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));
        if (assignment.getFileUrl() != null) {
        }
        assignmentRepository.deleteById(assignmentId);
    }

    @Override
    public AssignmentDetailResponse submitAssignment(AssignmentSubmitRequest request) throws IOException, GeneralSecurityException {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found with ID: " + request.getAssignmentId()));

        if (assignment.getDeadline() != null && assignment.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Assignment submission deadline has passed");
        }

        AssignmentDetail assignmentDetail = AssignmentDetail.builder()
                .assignment(assignment)
                .submitTime(LocalDateTime.now())
                .note(request.getNote())
                .fileUrl(request.getFileUrl())
                .studentUsername(request.getStudentUsername())
                .build();

        assignmentDetail = assignmentDetailRepository.save(assignmentDetail);

        return assignmentDetailMapper.toAssignmentResponse(assignmentDetail);
    }

    @Override
    public Page<AssignmentDetailResponse> getSubmissionsByAssignment(Integer assignmentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssignmentDetail> assignmentDetails = assignmentDetailRepository.findByAssignment_Id(assignmentId, pageable);
        return assignmentDetails.map(assignmentDetailMapper::toAssignmentResponse);
    }

    @Override
    public boolean checkSubmission(Integer assignmentId, String studentUsername) {
        return assignmentDetailRepository.existsByAssignment_IdAndStudentUsername(assignmentId, studentUsername);
    }

    @Override
    public List<AssignmentResponse> getAssignmentsByIds(ListIdRequest request){
        return getAssignmentResponsesFromIds(request.getIdsList());
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
                .collect(Collectors.toList());
    }
}

