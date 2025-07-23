package com.example.profile_service.repository.httpClient;

import com.example.profile_service.configuration.FeignRequestInterceptor;
import com.example.profile_service.dto.request.AccountRequest;
import com.example.profile_service.dto.request.AccountUpdateRequest;
import com.example.profile_service.dto.response.AccountResponse;
import com.example.profile_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "identity-service",
        url = "${app.services.identity}",
        configuration = FeignRequestInterceptor.class
)
public interface IdentityClient {

    @PostMapping(value = "/users/create-new-user", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<AccountResponse> createUser(@RequestBody AccountRequest accountRequest);

    @PostMapping(value = "/users/update-account", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<String> updateAccount(@RequestBody AccountUpdateRequest accountUpdateRequest);
}
