package com.example.api_gateway.service;

import com.example.api_gateway.dto.response.ApiResponse;
import com.example.api_gateway.dto.response.IntrospectResponse;
import reactor.core.publisher.Mono;

public interface IdentityService {
    Mono<ApiResponse<IntrospectResponse>> introspect(String token);
}
