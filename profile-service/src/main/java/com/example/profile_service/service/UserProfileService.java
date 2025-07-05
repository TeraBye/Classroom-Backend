package com.example.profile_service.service;

import com.example.profile_service.dto.request.UserProfileCreationRequest;
import com.example.profile_service.dto.response.UserProfileResponse;

import java.util.List;

public interface UserProfileService {
    UserProfileResponse createUserProfile(UserProfileCreationRequest request);
    List<UserProfileResponse> getAllProfile();
    UserProfileResponse getUserProfileByUsername(String username);
    void deleteUserProfile(String username);
}
