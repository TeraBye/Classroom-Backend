package com.example.question_service.repository;

import com.example.question_service.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    boolean existsByContentIgnoreCase(String content);
    List<Question> findBySubjectId(int subjectId);
}