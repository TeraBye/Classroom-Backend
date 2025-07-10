package com.example.chat_service.service.impl;

import com.example.chat_service.dto.request.ConversationCreationRequest;
import com.example.chat_service.dto.request.ListUsernameRequest;
import com.example.chat_service.dto.response.ConversationResponse;
import com.example.chat_service.dto.response.UserConversationResponse;
import com.example.chat_service.dto.response.UserProfileResponse;
import com.example.chat_service.entity.Conversation;
import com.example.chat_service.mapper.ConversationMapper;
import com.example.chat_service.repository.ConversationRepository;
import com.example.chat_service.repository.httpClient.ProfileClient;
import com.example.chat_service.service.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConversationServiceImpl implements ConversationService {
    ConversationRepository conversationRepository;

    ConversationMapper conversationMapper;
    ProfileClient profileClient;

    @Override
    public UserConversationResponse createConversation(ConversationCreationRequest request){
        UserProfileResponse userProfileResponse
                = profileClient.getUserProfileByUsername(
                        request.getReceiverUsername()).getResult();

        UserConversationResponse obj =  conversationMapper
                .toUserConversationResponse(request, userProfileResponse);

        Conversation conversation = conversationRepository
                .save(conversationMapper.toConversation(request));
        obj.setConver_id(conversation.getId());
        return obj;
    }

    @Override
    public List<UserConversationResponse> getConversations(String username){
        List<Conversation> conversations = conversationRepository
                .findBySenderUsernameOrReceiverUsername(username, username);

        ListUsernameRequest list = ListUsernameRequest.builder()
                .listUsername(extractOtherUsernames(conversations, username))
                .build();
        System.out.println("ben duoi");
        System.out.println(extractOtherUsernames(conversations, username));
        System.out.println("list ne: " + list.getListUsername());

        List<UserProfileResponse> users =
                profileClient.getListUser(list).getResult();

        log.error("eei:", users);

        return mapConversations(conversations, users, username, conversationMapper);

    }

    public List<String> extractOtherUsernames(List<Conversation> conversations, String username) {
        return conversations.stream()
                .map(conversation -> {
                    if (conversation.getSenderUsername().equals(username)) {
                        return conversation.getReceiverUsername();
                    } else {
                        return conversation.getSenderUsername();
                    }
                })
                .distinct() // Nếu muốn loại trùng lặp
                .toList();
    }

    public List<UserConversationResponse> mapConversations(
            List<Conversation> conversations,
            List<UserProfileResponse> userProfiles,
            String username,
            ConversationMapper mapper
    ) {
        // Tạo map username -> userProfile để tìm nhanh
        Map<String, UserProfileResponse> profileMap = userProfiles.stream()
                .collect(Collectors.toMap(UserProfileResponse::getUsername, Function.identity()));

        // Map từng conversation
        return conversations.stream()
                .map(conversation -> {
                    String otherUsername = conversation.getSenderUsername().equals(username)
                            ? conversation.getReceiverUsername()
                            : conversation.getSenderUsername();

                    UserProfileResponse otherUserProfile = profileMap.get(otherUsername);

                    return mapper.toUserConversationResponse2(conversation, otherUserProfile);
                })
                .toList();
    }



}
