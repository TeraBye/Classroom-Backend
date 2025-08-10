package com.example.exam_service.repository;

import com.example.exam_service.entity.ExamQuestion;
import com.example.exam_service.entity.ExamSubmissionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAnswerRepository extends JpaRepository<ExamSubmissionAnswer,Long> {
    Optional<ExamSubmissionAnswer> findBySubmissionIdAndQuestionId(Long submissionId, int questionId);
    List<ExamSubmissionAnswer> findAllBySubmissionId(Long submissionId);
}
