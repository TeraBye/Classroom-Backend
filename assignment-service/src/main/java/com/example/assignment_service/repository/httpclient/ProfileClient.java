package com.example.assignment_service.repository.httpclient;

import com.example.assignment_service.dto.request.ListUsernameRequest;
import com.example.assignment_service.dto.response.ApiResponse;
import com.example.assignment_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping(value = "/users/getListUserByListUsername", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<UserProfileResponse>> getListUserByListUsername(@RequestBody ListUsernameRequest usernames);
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getUserProfileByUsername(@PathVariable String username);
}
