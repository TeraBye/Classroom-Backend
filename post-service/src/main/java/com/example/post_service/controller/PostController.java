package com.example.post_service.controller;

import com.example.post_service.dto.request.PostCreationRequest;
import com.example.post_service.dto.response.ApiResponse;
import com.example.post_service.dto.response.ClassPostResponse;
import com.example.post_service.dto.response.PostResponse;
import com.example.post_service.dto.response.UserPostResponse;
import com.example.post_service.entity.Post;
import com.example.post_service.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/createPost")
    public ApiResponse<UserPostResponse> createPost(@RequestBody PostCreationRequest request) throws JsonProcessingException {
        return ApiResponse.<UserPostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @GetMapping("/getPostbyClass")
    public ApiResponse<List<UserPostResponse>> getPostByClassId(
            @RequestParam Integer classId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ApiResponse.<List<UserPostResponse>>builder()
                .result(postService.getPostsbyClass(classId, page, size))
                .build();
    }

    @GetMapping("/count-by-classrooms")
    public ApiResponse<List<ClassPostResponse>> getPostNumByClassIds(@RequestParam List<Integer> classIds) {
        return ApiResponse.<List<ClassPostResponse>>builder()
                .result(postService.getPostNumsByClassIds(classIds))
                .build();
    }

}
