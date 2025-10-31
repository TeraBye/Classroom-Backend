package com.example.assignment_service.repository;

import com.example.assignment_service.entity.AssignmentDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentDetailRepository extends JpaRepository<AssignmentDetail, Integer> {
    List<AssignmentDetail> findByAssignment_Id(Integer assignmentId);
    boolean existsByAssignment_IdAndStudentUsername(Integer assignmentId, String studentUsername);
    Optional<AssignmentDetail> findByAssignment_IdAndStudentUsername(Integer assignmentId, String studentUsername);

    List<AssignmentDetail> findByStudentUsername(String studentUsername);
}