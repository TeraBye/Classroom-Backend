package com.example.post_service.service.impl;


import com.example.post_service.dto.request.CommentCreationRequest;
import com.example.post_service.dto.request.ListUsernameRequest;
import com.example.post_service.dto.response.UserCommentResponse;
import com.example.post_service.dto.response.UserProfileResponse;
import com.example.post_service.entity.Comment;
import com.example.post_service.mapper.CommentMapper;
import com.example.post_service.repository.CommentRepository;
import com.example.post_service.repository.PostRepository;
import com.example.post_service.repository.httpClient.ProfileClient;
import com.example.post_service.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    PostRepository postRepository;
    ProfileClient profileClient;
    CommentMapper commentMapper;
    SimpMessagingTemplate messagingTemplate;

    @Override
    public UserCommentResponse createComment(CommentCreationRequest request){
        Comment comment = commentMapper.toComment(request);
        comment.setPost(postRepository.findById(request.getPost_id())
                .orElseThrow(() -> new RuntimeException("post not found")));

        comment = commentRepository.save(comment);

        UserProfileResponse userProfileResponse = profileClient
                .getUserProfileByUsername(comment.getUsername()).getResult();

        UserCommentResponse userCommentResponse = commentMapper
                .toCoCommentResponse(comment);
        userCommentResponse.setAvatar(userProfileResponse.getAvatar());
        userCommentResponse.setFullName(userProfileResponse.getFullName());

        messagingTemplate.convertAndSend(
                "/topic/comments/" + request.getPost_id(),
                userCommentResponse
        );

        return  userCommentResponse;
    }

    @Override
    public List<UserCommentResponse> getAllComment(Long postId, int page, int size) {
        Page<Comment> comments = commentRepository.findByPost_PostId(postId, PageRequest.of(page, size));

        // 2. Lấy danh sách username duy nhất (giữ thứ tự)
        List<String> usernameList = comments.stream()
                .map(Comment::getUsername)
                .filter(Objects::nonNull)
                .distinct() // loại trùng nhưng không giữ thứ tự tuyệt đối
                .collect(Collectors.toList());

        System.out.println(usernameList);
        // 3. Gọi profile service để lấy thông tin người dùng
        ListUsernameRequest request = ListUsernameRequest.builder()
                .listUsername(usernameList)
                .build();

        List<UserProfileResponse> users = profileClient.getListUser(request).getResult();
        System.out.println(users.size());

        System.out.println("=== Danh sách comments ===");
        comments.forEach(c -> System.out.println("Comment username: '" + c.getUsername() + "'"));

        System.out.println("=== Danh sách usernames gọi sang profile service ===");
        usernameList.forEach(u -> System.out.println("username: '" + u + "'"));

        System.out.println("=== Danh sách user trả về từ profile service ===");
        users.forEach(u -> System.out.println("username: '" + u.getUsername() + "'"));


        // Tạo Map<String, UserProfileResponse> từ danh sách user
        Map<String, UserProfileResponse> userMap = new HashMap<>();
        for (UserProfileResponse user : users) {
            String key = user.getUsername() != null ? user.getUsername().trim().toLowerCase() : "";
            if (!key.isEmpty()) {
                userMap.put(key, user);
            }
        }


        List<UserCommentResponse> result = comments.stream()
                .map(comment -> {
                    String key = comment.getUsername() != null ? comment.getUsername().trim().toLowerCase() : "";
                    UserProfileResponse user = userMap.get(key);

                    return UserCommentResponse.builder()
                            .commentId(comment.getCommentId())
                            .content(comment.getContent())
                            .username(comment.getUsername())
                            .fullName(user != null ? user.getFullName() : null)
                            .avatar(user != null ? user.getAvatar() : null)
                            .createdAt(comment.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return result;


    }
}
