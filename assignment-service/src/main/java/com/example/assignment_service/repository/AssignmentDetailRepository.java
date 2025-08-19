package com.example.assignment_service.repository;

import com.example.assignment_service.entity.AssignmentDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentDetailRepository extends JpaRepository<AssignmentDetail, Integer> {
    Page<AssignmentDetail> findByAssignment_Id(Integer assignmentId, Pageable pageable);
    boolean existsByAssignment_IdAndStudentUsername(Integer assignmentId, String studentUsername);
    AssignmentDetail findByAssignment_IdAndStudentUsername(Integer assignmentId, String studentUsername);
}