package com.example.question_service.repository;

import com.example.question_service.entity.QuestionVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionVersionRepository extends JpaRepository<QuestionVersion, Integer> {
    @Query("SELECT COALESCE(MAX(v.version), 0) FROM QuestionVersion v WHERE v.question.id = :questionId")
    Integer findMaxVersionByQuestionId(@Param("questionId") Integer questionId);

    Optional<QuestionVersion> findByQuestion_IdAndVersion(Integer questionId, Integer version);
}