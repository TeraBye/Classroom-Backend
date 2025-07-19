package com.example.assignment_service.service.Impl;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentSubmitRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.AssignmentDetailResponse;
import com.example.assignment_service.dto.response.AssignmentResponse;
import com.example.assignment_service.entity.Assignment;
import com.example.assignment_service.entity.AssignmentDetail;
import com.example.assignment_service.mapper.AssignmentDetailMapper;
import com.example.assignment_service.mapper.AssignmentMapper;
import com.example.assignment_service.repository.AssignmentDetailRepository;
import com.example.assignment_service.repository.AssignmentRepository;
import com.example.assignment_service.service.AssignmentService;
import com.example.assignment_service.service.FileStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentServiceImpl implements AssignmentService {
    AssignmentRepository assignmentRepository;
    AssignmentMapper assignmentMapper;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    FileStorageService fileStorageService;
    AssignmentDetailRepository assignmentDetailRepository;
    AssignmentDetailMapper assignmentDetailMapper;

    @Override
    public AssignmentResponse createAssignment(AssignmentCreateRequest request) throws GeneralSecurityException, IOException {
        // Upload file lên Cloudinary
        String fileUrl = fileStorageService.uploadFile(request.getFile(), request.getUsername(), Optional.empty(), "TEACHER");

        LocalDateTime deadline;
        try {
//            System.out.println("Raw deadline string: [" + request.getDeadline() + "]");
            deadline = LocalDateTime.parse(request.getDeadline().trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Sai định dạng deadline. Định dạng đúng: HH:mm:ss dd:MM:yyyy", e);
        }

        Assignment assignment = assignmentMapper.toAssignment(request);
        assignment.setAssignmentCode(request.getUsername() + "_assignment_" + deadline);
        assignment.setFileUrl(fileUrl);
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
    public AssignmentResponse updateAssignment(int assignmentId, AssignmentUpdateRequest request) {
        String formatted = request.getDeadline().format(formatter);
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
        Assignment assignment = null;
        if (assignmentOptional.isPresent()) {
            assignment = assignmentOptional.get();
        }
        if (!assignment.getDeadline().equals(request.getDeadline()) ||
                !assignment.getUsername().equals(request.getUsername())) {
            assignment.setAssignmentCode(request.getUsername() + "_assignment_" + formatted);
        }
        assignmentMapper.updateAssignment(assignment, request);
        assignmentRepository.save(assignment);
        return assignmentMapper.toAssignmentResponse(assignment);
    }

    @Override
    public void deleteAssignment(int assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }

    @Override
    public AssignmentDetailResponse submitAssignment(AssignmentSubmitRequest request) throws IOException, GeneralSecurityException {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found with ID: " + request.getAssignmentId()));

        if (assignment.getDeadline() != null && assignment.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Assignment submission deadline has passed");
        }

        // Upload file lên Google Drive và lấy URL
        String fileUrl = fileStorageService.uploadFile(request.getFile(), request.getStudentUsername(), Optional.of(request.getAssignmentId()), "STUDENT");

        AssignmentDetail assignmentDetail = AssignmentDetail.builder()
                .assignment(assignment)
                .submitTime(LocalDateTime.now())
                .fileUrl(fileUrl)
                .studentUsername(request.getStudentUsername())
                .build();

        assignmentDetail = assignmentDetailRepository.save(assignmentDetail);

        return assignmentDetailMapper.toAssignmentResponse(assignmentDetail);
    }


}
