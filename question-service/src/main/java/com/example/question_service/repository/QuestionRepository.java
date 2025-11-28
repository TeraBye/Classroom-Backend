package com.example.question_service.repository;

import com.example.question_service.entity.Question;
import com.example.question_service.enums.Level;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findBySubjectId(int subjectId);

    //Luan lam
    @Query("SELECT q FROM Question q WHERE q.subjectId = :subjectId AND q.level = :level ORDER BY RAND()")
    List<Question> findRandomBySubjectIdAndLevel(@Param("subjectId") Integer subjectId, @Param("level") Level level, Pageable pageable);

    long countBySubjectIdAndLevel(Integer subjectId, Level level);

    List<Question> findByIdIn(List<Integer> ids);

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

    @Query("SELECT q FROM Question q " +
            "WHERE q.deleted = false " +
            "AND (:subjectId IS NULL OR q.subjectId = :subjectId) " +
            "AND (:level IS NULL OR q.level = :level) " +
            "AND (:keyword IS NULL OR " +
            "q.content LIKE CONCAT('%', :keyword, '%') OR " +
            "q.optionA LIKE CONCAT('%', :keyword, '%') OR " +
            "q.optionB LIKE CONCAT('%', :keyword, '%') OR " +
            "q.optionC LIKE CONCAT('%', :keyword, '%') OR " +
            "q.optionD LIKE CONCAT('%', :keyword, '%')) " +
            "AND q.id > :cursor ORDER BY q.createdAt DESC")
    List<Question> findNextPageWithFilters(@Param("subjectId") Integer subjectId,
                                           @Param("level") Level level,
                                           @Param("keyword") String keyword,
                                           @Param("cursor") int cursor,
                                           @Param("size") int size);
}