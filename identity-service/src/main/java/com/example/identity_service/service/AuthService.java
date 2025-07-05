package com.example.identity_service.service;

import java.text.ParseException;

import com.example.identity_service.dto.request.AuthRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.request.LogoutRequest;
import com.example.identity_service.dto.request.RefreshRequest;
import com.example.identity_service.dto.response.AuthResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

public interface AuthService {
    AuthResponse authenticate(AuthRequest authRequest);

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
