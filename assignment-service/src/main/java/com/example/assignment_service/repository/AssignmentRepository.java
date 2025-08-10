package com.example.assignment_service.repository;

import com.example.assignment_service.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findByIdIn(List<Integer> ids);
}
