package com.example.exam_service.repository;

import com.example.exam_service.entity.ExamSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission,Long> {
    ExamSubmission findByStudentAndExamId(String student, Long examId);
    List<ExamSubmission> findByExam_Id(Long examId);
    List<ExamSubmission> findTop5ByStudentOrderBySubmittedAtDesc(String student);
}
