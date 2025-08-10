package org.example.scoreservice.repository.http;

import org.example.scoreservice.dto.response.ApiResponse;
import org.example.scoreservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", url = "${app.services.identity}")
public interface IdentityClient {
    @GetMapping(value = "/users/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> getUserById(@PathVariable int userId);
}
