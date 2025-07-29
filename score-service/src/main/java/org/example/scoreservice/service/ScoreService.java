package org.example.scoreservice.service;

import org.example.scoreservice.dto.request.ScoreRequest;
import org.example.scoreservice.dto.response.ClassroomResponse;
import org.example.scoreservice.dto.response.ScorePagingResponse;
import org.example.scoreservice.dto.response.ScoreResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScoreService {
    public List<ClassroomResponse> getAllClass(String classroomId, int cursor, Pageable pageable);
    public ScorePagingResponse<ScoreResponse> getPageScore(
            String classroomId, int studentId, int cursor, Pageable pageable);
    public ScoreResponse createScore(ScoreRequest scoreRequest);
    public ScoreResponse updateScore(ScoreRequest scoreRequest);
    public Boolean deleteScore(Integer scoreDetailId);

}
