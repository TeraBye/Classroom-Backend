package com.example.identity_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.identity_service.dto.request.UserRequestDTO;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequestDTO userRequestDTO);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUsersResponse(List<User> users);

    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
