package com.example.chat_service.repository.httpClient;

import com.example.chat_service.dto.request.ListUsernameRequest;
import com.example.chat_service.dto.response.ApiResponse;
import com.example.chat_service.dto.response.UserProfileResponse;
import org.springframework.http.MediaType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getUserProfileByUsername(
            @PathVariable("username") String username);

    @PostMapping("/users/getListUserByListUsername")
    ApiResponse<List<UserProfileResponse>> getListUser(
            @RequestBody ListUsernameRequest  request);
}
