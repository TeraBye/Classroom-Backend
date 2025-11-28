package com.example.post_service.service;

import com.example.post_service.dto.request.PostCreationRequest;
import com.example.post_service.dto.response.ClassPostResponse;
import com.example.post_service.dto.response.PostResponse;
import com.example.post_service.dto.response.UserPostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface PostService {
    UserPostResponse createPost(PostCreationRequest request) throws JsonProcessingException;
    List<UserPostResponse> getPostsbyClass(Integer classId, int page, int size);
    List<ClassPostResponse> getPostNumsByClassIds(List<Integer> classIds);
}
