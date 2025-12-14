package com.example.exam_service.repository;

import com.example.exam_service.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {
    List<Exam> findExamByClassId(int classId);
    List<Exam> findExamByTeacher(String teacher);
}
