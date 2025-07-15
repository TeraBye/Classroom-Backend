package com.example.identity_service.service;

import java.awt.print.Pageable;
import java.util.List;

import com.example.identity_service.dto.request.UserRequestDTO;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserPagingResponse;
import com.example.identity_service.dto.response.UserProfileResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    UserProfileResponse createUser(UserRequestDTO userRequestDTO);

    List<UserResponse> getAllUsers();

    UserResponse getUser(int userId);

    UserResponse updateUser(int userId, UserUpdateRequest userUpdateRequest);

    void deleteUser(int userId);

    UserProfileResponse getInfoUserIndex();

    UserPagingResponse<UserResponse> getPageUsersProfile(int cursor, Pageable pageable);
}
