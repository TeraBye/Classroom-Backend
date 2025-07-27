package com.example.classroom_service.repository;

import com.example.classroom_service.entity.Classroom;
import com.example.classroom_service.entity.ClassroomDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassroomDetailRepository extends JpaRepository<ClassroomDetail, Integer> {
    boolean existsByClassroomAndStudentUsername(Classroom classroom, String studentUsername);
    Optional<ClassroomDetail> findByClassroomAndStudentUsername(Classroom classroom, String studentUsername);
    Page<ClassroomDetail> findByStudentUsername(String studentUsername, Pageable pageable);
    @Query("SELECT cd.studentUsername FROM ClassroomDetail cd WHERE cd.classroom.id = :classroomId")
    Page<String> findStudentUsernamesByClassroomId(@Param("classroomId") int classroomId, Pageable pageable);
}