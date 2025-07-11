package com.example.assignment_service.service.Impl;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.AssignmentResponse;
import com.example.assignment_service.entity.Assignment;
import com.example.assignment_service.mapper.AssignmentMapper;
import com.example.assignment_service.repository.AssignmentRepository;
import com.example.assignment_service.service.AssignmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentServiceImpl implements AssignmentService {
    AssignmentRepository assignmentRepository;
    AssignmentMapper assignmentMapper;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd:MM:yyyy");

    @Override
    public AssignmentResponse createAssignment(AssignmentCreateRequest request) {
        String formatted = request.getDeadline().format(formatter);
        Assignment assignment = assignmentMapper.toAssignment(request);
        assignment.setAssignmentCode(request.getUsername() + "_assignment_" + formatted);
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
}
