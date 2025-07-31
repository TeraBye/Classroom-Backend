package com.example.post_service.service;

import com.example.post_service.dto.request.CommentCreationRequest;
import com.example.post_service.dto.response.UserCommentResponse;

import java.util.List;

public interface CommentService {
    UserCommentResponse createComment(CommentCreationRequest request);
    List<UserCommentResponse> getAllComment(Long postId, int page, int size);
}
