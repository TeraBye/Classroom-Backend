package com.example.profile_service.service;

import com.example.profile_service.dto.request.UserProfileCreationRequest;
import com.example.profile_service.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserProfileService {
    UserProfileResponse createUserProfile(UserProfileCreationRequest request);
    List<UserProfileResponse> getAllProfile();
    UserProfileResponse getUserProfileByUsername(String username);
    void deleteUserProfile(String username);
    List<UserProfileResponse> getUserProfileByListUsername(List<String> usernames);
    Page<UserProfileResponse> searchUsers(String q, int page, int size);
}
