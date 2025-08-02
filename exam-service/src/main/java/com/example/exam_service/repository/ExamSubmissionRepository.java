package com.example.exam_service.repository;

import com.example.exam_service.entity.ExamSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission,Long> {
    ExamSubmission findByStudentAndExamId(String student, Long examId);
}
