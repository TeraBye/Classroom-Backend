package org.example.scoreservice.mapper;

import org.example.scoreservice.dto.response.ScoreResponse;
import org.example.scoreservice.entity.ScoreDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScoreMapper {
    ScoreResponse toScoreResponse(ScoreDetail scoreDetail);
    List<ScoreResponse> toListScoreResponse(List<ScoreDetail> scoreDetailList);
}
