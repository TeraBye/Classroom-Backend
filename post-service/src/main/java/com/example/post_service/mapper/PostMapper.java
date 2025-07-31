package com.example.post_service.mapper;

import com.example.post_service.dto.request.AssignmentCreateRequest;
import com.example.post_service.dto.request.PostCreationRequest;
import com.example.post_service.dto.response.AssignmentResponse;
import com.example.post_service.dto.response.UserPostResponse;
import com.example.post_service.dto.response.UserProfileResponse;
import com.example.post_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostCreationRequest postCreationRequest);

    @Mapping(source = "title", target = "name")
    AssignmentCreateRequest toAssignmentCreateRequest(
            PostCreationRequest request);

    @Mappings({
            @Mapping(source = "assignment.id", target = "assignmentId"),
            @Mapping(source = "user.avatar", target = "avatar"),
            @Mapping(source = "user.fullName", target = "fullName"),
            @Mapping(source = "post.deadline", target = "deadline"),
            @Mapping(source = "post.fileUrl", target = "fileUrl"),
            @Mapping(source = "post.username", target = "username"),
            @Mapping(source = "post.title", target = "title"),
            @Mapping(source = "post.content", target = "content")
    })
    UserPostResponse mapToUserPostResponse(Long postId,
                                           LocalDateTime createdAt,
                                           AssignmentResponse assignment,
                                           UserProfileResponse user,
                                           PostCreationRequest post);

    @Mapping(source = "post.postId", target = "postId")
    @Mapping(source = "post.assignmentId", target = "assignmentId")
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "post.createAt", target = "createdAt")
    @Mapping(source = "post.title", target = "title")
    @Mapping(source = "post.content", target = "content")
    @Mapping(source = "post.username", target = "username")
    @Mapping(source = "assignment.deadline", target = "deadline")
    @Mapping(source = "assignment.fileUrl", target = "fileUrl")
    UserPostResponse toResponse(Post post, AssignmentResponse assignment, UserProfileResponse user);
}
