package com.example.classroom_service.repository;

import com.example.classroom_service.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    @Query("""
            SELECT s FROM Subject s
            WHERE (:username IS NULL OR s.id IN (
                SELECT ts.subject.id FROM TeacherSubject ts WHERE ts.teacherUsername = :username
            ))
            """)
    Page<Subject> findByTeacher_Username(String username, Pageable pageable);

    Optional<Subject> findByCode(String code);
}