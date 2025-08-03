package com.example.exam_service.repository;

import com.example.exam_service.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion,Long> {
    List<ExamQuestion> findByExamId(Long examId);
}
