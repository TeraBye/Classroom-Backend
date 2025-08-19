package com.example.profile_service.controller;

import com.example.profile_service.dto.request.*;
import com.example.profile_service.dto.response.ApiResponse;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("/users")
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping
    public ApiResponse<UserProfileResponse> createUser(@RequestBody UserProfileCreationRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.createUserProfile(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserProfileResponse>> getAllProfiles(){
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllProfile())
                .build();
    }

    @GetMapping("/{username}")
    public ApiResponse<UserProfileResponse> getUserProfileByUsername(@PathVariable String username){
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getUserProfileByUsername(username))
                .build();
    }


    @DeleteMapping("/{username}")
    public ApiResponse<String> deleteUserProfile(@PathVariable String username){
        userProfileService.deleteUserProfile(username);
        return ApiResponse.<String>builder()
                .result("deleted")
                .build();
    }

    @PostMapping("/getListUserByListUsername")
    public ApiResponse<List<UserProfileResponse>> getListUserByListUsername(
            @RequestBody ListUsernameRequest usernames
            ){
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getUserProfileByListUsername(usernames.getListUsername()))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<UserProfileResponse>> searchUsers(
            @RequestParam("q") String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<Page<UserProfileResponse>>builder()
                .result(userProfileService.searchUsers(q, page, size))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<String> createUser(
            @RequestBody CreateUserRequest createUserRequest
    ){
        if (userProfileService.createUser(createUserRequest)){
            return ApiResponse.<String>builder()
                    .code(200)
                    .result("created")
                    .build();
        }
        else {
            return ApiResponse.<String>builder()
                    .code(400)
                    .result("Username already existed!")
                    .build();
        }
    }

    @PostMapping("/update")
    public ApiResponse<String> updateUser(
            @RequestBody UpdateUserRequest updateUserRequest
    ){
        if (Objects.equals(userProfileService.updateUser(updateUserRequest), "")){
            return ApiResponse.<String>builder()
                    .code(200)
                    .result("updated")
                    .build();
        }
        else {
            return ApiResponse.<String>builder()
                    .code(400)
                    .result(userProfileService.updateUser(updateUserRequest))
                    .build();
        }
    }

    @PutMapping("/update-profile")
    public ApiResponse<UserProfileResponse> updateProfile(@RequestBody UpdateProfileRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateProfile(request))
                .build();
    }
}
