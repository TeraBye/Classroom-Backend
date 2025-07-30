package com.example.question_service.repository;

import com.example.question_service.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    boolean existsByContentIgnoreCase(String content);
    List<Question> findBySubjectId(int subjectId);

    @Query("SELECT q FROM Question q WHERE q.subjectId = :subjectId AND q.id > :cursor ORDER BY q.id ASC")
    List<Question> findNextPageScore(
            @Param("subjectId") Integer subjectId,@Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT DISTINCT q.subjectId FROM Question q WHERE q.subjectId > :cursor ORDER BY q.subjectId ASC")
    List<Integer> findAllNextPageSubjectQuestion(
            @Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT DISTINCT q.subjectId FROM Question q WHERE q.subjectId > :cursor AND q.subjectId = :subjectId ORDER BY q.subjectId ASC ")
    List<Integer> findDistinctNextPageListSubject(
            @Param("subjectId") Integer subjectId, @Param("cursor") int cursor, Pageable pageable);

    int countBySubjectId(Integer subjectId);
}