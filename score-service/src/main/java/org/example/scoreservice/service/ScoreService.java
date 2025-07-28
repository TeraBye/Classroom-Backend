package org.example.scoreservice.service;

import org.example.scoreservice.dto.request.ScoreRequest;
import org.example.scoreservice.dto.response.ScorePagingResponse;
import org.example.scoreservice.dto.response.ScoreResponse;
import org.springframework.data.domain.Pageable;

public interface ScoreService {
    public ScorePagingResponse<ScoreResponse> getPageScore(int cursor, Pageable pageable);
    public ScoreResponse createScore(ScoreRequest scoreRequest);
    public ScoreResponse updateScore(ScoreRequest scoreRequest);
    public Boolean deleteScore(Integer scoreDetailId);

}
