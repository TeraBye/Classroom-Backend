package org.example.scoreservice.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.scoreservice.dto.request.ScoreRequest;
import org.example.scoreservice.dto.response.ScorePagingResponse;
import org.example.scoreservice.dto.response.ScoreResponse;
import org.example.scoreservice.entity.ScoreDetail;
import org.example.scoreservice.enums.TYPEOFSCORE;
import org.example.scoreservice.exception.BusinessException;
import org.example.scoreservice.mapper.ScoreMapper;
import org.example.scoreservice.repository.ScoreRepository;
import org.example.scoreservice.service.ScoreService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreServiceImpl implements ScoreService {
    ScoreRepository scoreRepository;
    ScoreMapper scoreMapper;

    @Override
    public ScorePagingResponse<ScoreResponse> getPageScore(int cursor, Pageable pageable){
        List<ScoreDetail> scoreDetailList = scoreRepository.findNextPage(cursor,pageable);

        boolean hasNext = scoreDetailList.size() == pageable.getPageSize();
        int lastCursor = scoreDetailList.isEmpty() ? cursor : scoreDetailList.getLast().getScoreDetailId();

        return new ScorePagingResponse<>(scoreMapper.toListScoreResponse(scoreDetailList), lastCursor, hasNext);
    }

    @Override
    public ScoreResponse createScore(ScoreRequest scoreRequest){
        TYPEOFSCORE type = TYPEOFSCORE.valueOf(String.valueOf(scoreRequest.getTypeofscore()).toUpperCase());

        Optional<ScoreDetail> scoreDetailOptional = scoreRepository
                .findByClassroomIdAndStudentIdAndTypeofscore(
                        scoreRequest.getClassroomId(),
                        scoreRequest.getStudentId(),
                        type);
        if (scoreDetailOptional.isEmpty()){
            ScoreDetail scoreDetail1 = ScoreDetail.builder()
                    .classroomId(scoreRequest.getClassroomId())
                    .studentId(scoreRequest.getStudentId())
                    .score(scoreRequest.getScore())
                    .typeofscore(type).build();

            return scoreMapper.toScoreResponse(scoreRepository.save(scoreDetail1));
        }
        throw new BusinessException("This record already exist!");
    }

    @Override
    public ScoreResponse updateScore(ScoreRequest scoreRequest){
        TYPEOFSCORE type = TYPEOFSCORE.valueOf(String.valueOf(scoreRequest.getTypeofscore()).toUpperCase());

        Optional<ScoreDetail> scoreDetailOptional = scoreRepository
                .findById(scoreRequest.getScoreDetailId());

        if (scoreDetailOptional.isPresent()){
            ScoreDetail scoreDetail = scoreDetailOptional.get();
            scoreDetail.setScore(scoreRequest.getScore());
            scoreDetail.setTypeofscore(type);

            return scoreMapper.toScoreResponse(scoreRepository.save(scoreDetail));
        }
        throw new BusinessException("Can not find the record!");
    }

    @Override
    public Boolean deleteScore(Integer scoreDetailId){
        Optional<ScoreDetail> scoreDetailOptional = scoreRepository
                .findById(scoreDetailId);

        if (scoreDetailOptional.isPresent()){
            scoreRepository.delete(scoreDetailOptional.get());
            return true;
        }
        throw new BusinessException("Can not find the record!");
    }
}
