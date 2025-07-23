package com.example.assignment_service.repository;

import com.example.assignment_service.entity.AssignmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentDetailRepository extends JpaRepository<AssignmentDetail, Integer> {
}