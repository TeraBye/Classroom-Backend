package com.example.exam_service.repository;

import com.example.exam_service.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion,Long> {
    List<ExamQuestion> findByExamId(Long examId);

    @Query("SELECT eq FROM ExamQuestion eq " +
            "JOIN eq.exam e " +
            "LEFT JOIN ExamSubmission es ON e.id = es.exam.id " +
            "WHERE eq.questionId = :questionId " +
            "AND (es.id IS NULL AND (e.beginTime OR e.beginTime > :now))")
    List<ExamQuestion> findUnstartedExamsByQuestionId(@Param("questionId") int questionId, @Param("now") LocalDateTime now);
}
