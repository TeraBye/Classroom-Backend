package com.example.question_service.repository;

import com.example.question_service.entity.QuestionVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionVersionRepository extends JpaRepository<QuestionVersion, Integer> {
}