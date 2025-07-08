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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentServiceImpl implements AssignmentService {
    AssignmentRepository assignmentRepository;
    AssignmentMapper assignmentMapper;
    @Override
    public AssignmentResponse createAssignment(AssignmentCreateRequest request) {
        Assignment assignment = assignmentMapper.toAssignment(request);
        assignment.setAssignmentCode(request.getUsername() + "_assignment_" + request.getDeadline().toString());
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
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
        Assignment assignment = null;
        if (assignmentOptional.isPresent()) {
            assignment = assignmentOptional.get();
        }
        if (assignment.getDeadline() != request.getDeadline() || !assignment.getUsername().equals(request.getUsername())) {
            assignment.setAssignmentCode(request.getUsername() + "_assignment_" + request.getDeadline().toString());
        }
        assignmentMapper.updateAssignment(assignment, request);
        return assignmentMapper.toAssignmentResponse(assignment);
    }

    @Override
    public void deleteAssignment(int assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }
}
