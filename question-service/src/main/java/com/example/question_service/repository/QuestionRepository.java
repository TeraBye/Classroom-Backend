package com.example.question_service.repository;

import com.example.question_service.entity.Question;
import com.example.question_service.enums.Level;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    boolean existsByContentIgnoreCase(String content);
    List<Question> findBySubjectId(int subjectId);

    //Luan lam
    @Query("SELECT q FROM Question q WHERE q.subjectId = :subjectId AND q.level = :level ORDER BY RAND()")
    List<Question> findRandomBySubjectIdAndLevel(@Param("subjectId") Integer subjectId, @Param("level") Level level, Pageable pageable);

    long countBySubjectIdAndLevel(Integer subjectId, Level level);

    List<Question> findByIdIn(List<Integer> ids);
}