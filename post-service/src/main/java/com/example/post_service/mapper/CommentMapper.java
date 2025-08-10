package com.example.post_service.mapper;

import com.example.post_service.dto.request.CommentCreationRequest;
import com.example.post_service.dto.response.UserCommentResponse;
import com.example.post_service.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentCreationRequest request);
    UserCommentResponse toCoCommentResponse(Comment comment);
}
