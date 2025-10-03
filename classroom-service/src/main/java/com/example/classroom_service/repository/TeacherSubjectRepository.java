package com.example.classroom_service.repository;

import com.example.classroom_service.entity.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
}