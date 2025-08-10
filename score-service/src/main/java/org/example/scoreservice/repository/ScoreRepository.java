package org.example.scoreservice.repository;

import org.example.scoreservice.entity.ScoreDetail;
import org.example.scoreservice.enums.TYPEOFSCORE;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreDetail, Integer> {

    @Query("SELECT s FROM ScoreDetail s WHERE s.classroomId = :classroomId AND s.scoreDetailId > :cursor ORDER BY s.scoreDetailId ASC")
    List<ScoreDetail> findNextPageScore(
            @Param("classroomId") String classroomId, @Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT DISTINCT s.classroomId FROM ScoreDetail s WHERE s.classroomId > :cursor ORDER BY s.classroomId ASC")
    List<String> findNextPageClassScore(
            @Param("cursor") int cursor, Pageable pageable);

    int countByClassroomId(String classroomId);

    @Query("SELECT DISTINCT s.classroomId FROM ScoreDetail s WHERE s.classroomId LIKE %:keyword%")
    List<String> findDistinctClassroomIdsByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<ScoreDetail> findAllByClassroomIdAndStudentId(
            String classroomId, int studentId, Pageable pageable);


    Optional<ScoreDetail> findByClassroomIdAndStudentIdAndTypeofscore(
            String classroomId, int studentId, TYPEOFSCORE typeofscore);
}
