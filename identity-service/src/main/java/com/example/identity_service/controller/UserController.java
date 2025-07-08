package com.example.identity_service.controller;

import java.util.List;

import com.example.identity_service.dto.response.UserProfileResponse;
import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.identity_service.dto.request.UserRequestDTO;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.ApiResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    UserMapper userMapper;

    @PostMapping("/registration")
    public ApiResponse<UserProfileResponse> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userService.createUser(userRequestDTO))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable int userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("userId") int userId, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, userUpdateRequest))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("delete").build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserProfileResponse> getMyInfo() {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userService.getInfoUserIndex())
                .build();
    }
}
