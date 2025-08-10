package com.example.profile_service.service;

import com.example.profile_service.dto.request.UpdateUserRequest;
import com.example.profile_service.dto.request.UserProfileCreationRequest;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.dto.request.CreateUserRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserProfileService {
    UserProfileResponse createUserProfile(UserProfileCreationRequest request);
    List<UserProfileResponse> getAllProfile();
    UserProfileResponse getUserProfileByUsername(String username);
    void deleteUserProfile(String username);
    List<UserProfileResponse> getUserProfileByListUsername(List<String> usernames);
    Page<UserProfileResponse> searchUsers(String q, int page, int size);

    boolean createUser(CreateUserRequest createUserRequest);
    String updateUser(UpdateUserRequest updateUserRequest);
}
