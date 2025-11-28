package com.example.classroom_service.repository;

import com.example.classroom_service.entity.JoinRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Integer> {
    Page<JoinRequest> findByTeacherUsernameAndStatus(String teacherUsername, String status, Pageable pageable);

    Optional<JoinRequest> findByClassroom_IdAndStudentUsername(Integer classroomId, String studentUsername);
}