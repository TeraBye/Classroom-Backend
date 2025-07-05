package com.example.identity_service.service;

import java.util.List;

import com.example.identity_service.dto.request.UserRequestDTO;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserProfileResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;

public interface UserService {
    UserProfileResponse createUser(UserRequestDTO userRequestDTO);

    List<UserResponse> getAllUsers();

    UserResponse getUser(int userId);

    UserResponse updateUser(int userId, UserUpdateRequest userUpdateRequest);

    void deleteUser(int userId);

    UserResponse getInfoUserIndex();
}
;
