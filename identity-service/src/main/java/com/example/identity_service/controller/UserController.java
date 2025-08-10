package com.example.identity_service.controller;

import java.util.List;

import com.example.identity_service.dto.request.AccountRequest;
import com.example.identity_service.dto.request.AccountUpdateRequest;
import com.example.identity_service.dto.response.*;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.identity_service.dto.request.UserRequestDTO;
import com.example.identity_service.dto.request.UserUpdateRequest;
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

    @PostMapping("/regis-list")
    public ApiResponse<List<UserProfileResponse>> createListUser(@RequestBody @Valid List<UserRequestDTO> listUserRequestDTO) {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(listUserRequestDTO.stream()
                        .map(userService::createUser)
                        .toList())
                .build();
    }

    @GetMapping("/get-list-users")
    public ApiResponse<UserPagingResponse<UserDetailsResponse>> getListUsers(
            @RequestParam int cursor,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page,size);
        return ApiResponse.<UserPagingResponse<UserDetailsResponse>>builder()
                .result(userService.getPageUsersProfile(cursor, pageable))
                .build();
    }

    @PostMapping("/delete/{userId}")
    public ApiResponse<String> deleteUserAccountByUserId(@PathVariable int userId){
        if (userService.deleteUserAccountByUserId(userId)){
            return ApiResponse.<String>builder()
                    .code(200)
                    .result("deleted")
                    .build();
        }
        return ApiResponse.<String>builder()
                .code(400)
                .result("failed")
                .build();
    }

    @PostMapping(value = "/create-new-user")
    ApiResponse<AccountResponse> createUser(@RequestBody AccountRequest accountRequest) {
        return ApiResponse.<AccountResponse>builder()
                .result(userService.createNewUser(accountRequest))
                .build();
    }

    @PostMapping("/update-account")
    ApiResponse<String> updateAccount(
            @RequestBody AccountUpdateRequest accountUpdateRequest
            ){
        if (userService.updateAccount(accountUpdateRequest)){
            return ApiResponse.<String>builder()
                    .code(200)
                    .result("account updated!")
                    .build();
        }
        return ApiResponse.<String>builder()
                .code(400)
                .result("account update failed!")
                .build();
    }
}

