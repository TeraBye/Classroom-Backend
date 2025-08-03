package com.example.post_service.controller;

import com.example.post_service.dto.request.CommentCreationRequest;
import com.example.post_service.dto.response.ApiResponse;
import com.example.post_service.dto.response.UserCommentResponse;
import com.example.post_service.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @PostMapping("/createComment")
    ApiResponse<UserCommentResponse> createComment(
            @RequestBody CommentCreationRequest request) {
        return ApiResponse.<UserCommentResponse>builder()
                .result(commentService.createComment(request))
                .build();
    }

    @GetMapping("/comments/{postId}")
    ApiResponse<List<UserCommentResponse>> listComments(
            @PathVariable Long postId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ApiResponse.<List<UserCommentResponse>>builder()
                .result(commentService.getAllComment(postId, page, size))
                .build();
    }
}
