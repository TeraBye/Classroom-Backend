package com.example.classroom_service.repository;

import com.example.classroom_service.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    Optional<Classroom> findByClassCode(String classCode);
    boolean existsByClassCode(String classCode);

    @Query("SELECT c FROM Classroom c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(c.subject.name) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Classroom> searchClassrooms(@Param("q") String q, Pageable pageable);

    @Query("SELECT c FROM Classroom c WHERE c.teacherUsername = :username")
    Page<Classroom> findByTeacherUsername(@Param("username") String username, Pageable pageable);

}