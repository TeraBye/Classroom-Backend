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

    @Query("SELECT s FROM ScoreDetail s WHERE s.scoreDetailId > :cursor ORDER BY s.scoreDetailId ASC ")
    List<ScoreDetail> findNextPage(@Param("cursor") int cursor, Pageable pageable);

    Optional<ScoreDetail> findByClassroomIdAndStudentIdAndTypeofscore(String classroomId, String studentId, TYPEOFSCORE typeofscore);
}
