package com.example.post_service.service.impl;

import com.example.post_service.dto.request.AssignmentCreateRequest;
import com.example.post_service.dto.request.ListIdRequest;
import com.example.post_service.dto.request.PostCreationRequest;
import com.example.post_service.dto.response.AssignmentResponse;
import com.example.post_service.dto.response.PostResponse;
import com.example.post_service.dto.response.UserPostResponse;
import com.example.post_service.dto.response.UserProfileResponse;
import com.example.post_service.entity.Post;
import com.example.post_service.mapper.PostMapper;
import com.example.post_service.repository.PostRepository;
import com.example.post_service.repository.httpClient.AssignmentClient;
import com.example.post_service.repository.httpClient.ProfileClient;
import com.example.post_service.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    ProfileClient profileClient;
    AssignmentClient assignmentClient;
    PostMapper postMapper;
    SimpMessagingTemplate messagingTemplate;

    @Override
    public UserPostResponse createPost(PostCreationRequest request){
        AssignmentResponse assignmentResponse =  assignmentClient.createAssignment(
                postMapper.toAssignmentCreateRequest(request)
        ).getResult();

        UserProfileResponse userProfileResponse = profileClient.getUserProfileByUsername(
                request.getUsername()
        ).getResult();

        Post post = postMapper.toPost(request);
        post.setCreateAt(LocalDateTime.now());
        post.setAssignmentId(assignmentResponse.getId());
        post = postRepository.save(post);

        UserPostResponse response = postMapper.mapToUserPostResponse(
                post.getPostId(),
                post.getCreateAt(),
                assignmentResponse,
                userProfileResponse,
                request
        );

        messagingTemplate.convertAndSend(
                "/topic/posts/" + request.getClassId(),
                response
        );

        return response;
    }

    @Override
    public List<UserPostResponse> getPostsbyClass(Integer classId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Post> posts = postRepository.
                findByClassIdOrderByCreateAtDesc(classId, pageRequest).getContent();

        if (posts.isEmpty()) {
            log.warn("No posts found for classId {}", classId);
            return Collections.emptyList();
        }

        List<Integer> assignmentIds = posts.stream()
                .map(post -> post.getAssignmentId() != null ? post.getAssignmentId() : 0)
                .toList();

        List<AssignmentResponse> assignments = assignmentClient
                .getAssignmentsByListId(ListIdRequest.builder()
                        .idsList(assignmentIds)
                        .build()).getResult();

        UserProfileResponse userProfileResponse = profileClient
                .getUserProfileByUsername(posts.get(0).getUsername())
                .getResult();

        return IntStream.range(0, posts.size())
                .mapToObj(i -> {
                    Post post = posts.get(i);
                    AssignmentResponse assignment = assignments.get(i); // Có thể null nếu id = 0
                    return postMapper.toResponse(post, assignment, userProfileResponse);
                })
                .toList();

    }

}
