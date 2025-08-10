package org.example.scoreservice.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.scoreservice.dto.request.ScoreRequest;
import org.example.scoreservice.dto.response.ClassroomResponse;
import org.example.scoreservice.dto.response.ScorePagingResponse;
import org.example.scoreservice.dto.response.ScoreResponse;
import org.example.scoreservice.dto.response.UserResponse;
import org.example.scoreservice.entity.ScoreDetail;
import org.example.scoreservice.enums.TYPEOFSCORE;
import org.example.scoreservice.exception.BusinessException;
import org.example.scoreservice.mapper.ScoreMapper;
import org.example.scoreservice.repository.ScoreRepository;
import org.example.scoreservice.repository.http.IdentityClient;
import org.example.scoreservice.service.ScoreService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreServiceImpl implements ScoreService {
    ScoreRepository scoreRepository;
    ScoreMapper scoreMapper;
    IdentityClient identityClient;

    @Override
    public List<ClassroomResponse> getAllClass(String classroomId, int cursor, Pageable pageable) {
        List<String> classList;

        if (!classroomId.isEmpty()){
            classList = scoreRepository.findDistinctClassroomIdsByKeyword(classroomId, pageable);
        } else {
            classList = scoreRepository.findNextPageClassScore(cursor, pageable);
        }

        if (!classList.isEmpty()) {
            List<ClassroomResponse> classroomResponses = new ArrayList<>();
            for (String c : classList) {
                int total = scoreRepository.countByClassroomId(c);
                classroomResponses.add(new ClassroomResponse(c,total));
            }
            return classroomResponses;
        }
        throw new BusinessException("Can not find any classroom!");
    }

    @Override
    public ScorePagingResponse<ScoreResponse> getPageScore(String classroomId, int studentId, int cursor, Pageable pageable) {
        List<ScoreDetail> scoreDetailList;
        if (!(studentId == -999)) {
            scoreDetailList = scoreRepository.findAllByClassroomIdAndStudentId(classroomId, studentId, pageable);
        } else {
            scoreDetailList = scoreRepository.findNextPageScore(classroomId, cursor, pageable);
        }

        boolean hasNext = scoreDetailList.size() == pageable.getPageSize();
        int lastCursor = scoreDetailList.isEmpty() ? cursor : scoreDetailList.getLast().getScoreDetailId();

        return new ScorePagingResponse<>(scoreMapper.toListScoreResponse(scoreDetailList), lastCursor, hasNext);
    }

    @Override
    public ScoreResponse createScore(ScoreRequest scoreRequest) {
        TYPEOFSCORE type = TYPEOFSCORE.valueOf(String.valueOf(scoreRequest.getTypeofscore()).toUpperCase());

        Optional<ScoreDetail> scoreDetailOptional = scoreRepository
                .findByClassroomIdAndStudentIdAndTypeofscore(
                        scoreRequest.getClassroomId(),
                        scoreRequest.getStudentId(),
                        type);
        if (scoreDetailOptional.isEmpty()) {

            try {
                UserResponse userResponse = identityClient.getUserById(scoreRequest.getStudentId()).getResult();
            } catch (Exception e) {
                throw new BusinessException("Can not find the student!");
            }

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
    public ScoreResponse updateScore(ScoreRequest scoreRequest) {
        TYPEOFSCORE type = TYPEOFSCORE.valueOf(String.valueOf(scoreRequest.getTypeofscore()).toUpperCase());

        Optional<ScoreDetail> scoreDetailOptional = scoreRepository
                .findById(scoreRequest.getScoreDetailId());

        if (scoreDetailOptional.isPresent()) {
            ScoreDetail scoreDetail = scoreDetailOptional.get();
            scoreDetail.setScore(scoreRequest.getScore());
            scoreDetail.setTypeofscore(type);

            return scoreMapper.toScoreResponse(scoreRepository.save(scoreDetail));
        }
        throw new BusinessException("Can not find the record!");
    }

    @Override
    public Boolean deleteScore(Integer scoreDetailId) {
        Optional<ScoreDetail> scoreDetailOptional = scoreRepository
                .findById(scoreDetailId);

        if (scoreDetailOptional.isPresent()) {
            scoreRepository.delete(scoreDetailOptional.get());
            return true;
        }
        throw new BusinessException("Can not find the record!");
    }
}
