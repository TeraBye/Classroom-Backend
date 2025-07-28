package org.example.scoreservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.scoreservice.dto.request.ScoreRequest;
import org.example.scoreservice.dto.response.ApiResponse;
import org.example.scoreservice.dto.response.ScorePagingResponse;
import org.example.scoreservice.dto.response.ScoreResponse;
import org.example.scoreservice.service.ScoreService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/score")
public class ScoreController {

    ScoreService scoreService;

    @GetMapping("/get-list-score")
    public ApiResponse<ScorePagingResponse<ScoreResponse>> getListUsers(
            @RequestParam int cursor,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page,size);
        return ApiResponse.<ScorePagingResponse<ScoreResponse>>builder()
                .result(scoreService.getPageScore(cursor, pageable))
                .build();
    }

    @PostMapping("create")
    public ApiResponse<ScoreResponse> createScore(@RequestBody ScoreRequest scoreRequest){
        return ApiResponse.<ScoreResponse>builder().result(scoreService.createScore(scoreRequest)).build();
    }

    @PutMapping("/update")
    public ApiResponse<ScoreResponse> updateScore(@RequestBody ScoreRequest scoreRequest){
        return ApiResponse.<ScoreResponse>builder().result(scoreService.updateScore(scoreRequest)).build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<Boolean> deleteScore(@RequestParam Integer scoreDetailId){
        return ApiResponse.<Boolean>builder().result(scoreService.deleteScore(scoreDetailId)).build();
    }
}
