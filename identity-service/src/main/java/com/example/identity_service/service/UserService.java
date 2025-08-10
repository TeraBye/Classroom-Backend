package com.example.identity_service.service;

import java.util.List;

import com.example.identity_service.dto.request.AccountRequest;
import com.example.identity_service.dto.request.AccountUpdateRequest;
import com.example.identity_service.dto.request.UserRequestDTO;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.*;
import com.example.identity_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserProfileResponse createUser(UserRequestDTO userRequestDTO);

    List<UserResponse> getAllUsers();

    UserResponse getUser(int userId);

    UserResponse updateUser(int userId, UserUpdateRequest userUpdateRequest);

    void deleteUser(int userId);

    UserProfileResponse getInfoUserIndex();

    UserPagingResponse<UserDetailsResponse> getPageUsersProfile(int cursor, Pageable pageable);

    AccountResponse createNewUser(AccountRequest accountRequest);
    boolean deleteUserAccountByUserId(int userId);
    boolean updateAccount(AccountUpdateRequest accountUpdateRequest);

}
