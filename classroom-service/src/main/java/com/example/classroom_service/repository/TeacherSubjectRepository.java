package com.example.classroom_service.repository;

import com.example.classroom_service.entity.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
    Optional<TeacherSubject> findByTeacherUsernameAndSubject_Id(String teacherUsername, int subjectId);
}