package com.example.identity_service.controller;

import java.text.ParseException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.identity_service.dto.request.AuthRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.request.LogoutRequest;
import com.example.identity_service.dto.request.RefreshRequest;
import com.example.identity_service.dto.response.ApiResponse;
import com.example.identity_service.dto.response.AuthResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.service.AuthService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/token")
    ApiResponse<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        var result = authService.authenticate(authRequest);
        return ApiResponse.<AuthResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authService.logout(logoutRequest);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        var result = authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder().result(result).build();
    }

    @PostMapping("/admin-login")
    ApiResponse<AuthResponse> loginByAdmin(@RequestBody AuthRequest authRequest, HttpServletResponse response){
        var result = authService.authenticate(authRequest);
        String token = result.getToken();
        Cookie cookie = new Cookie("token",token);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        cookie.setAttribute("SameSite", "Strict"); // hoặc "Lax", "None" (None nếu dùng cross-origin)

        result.setToken("");  // ẩn token
        response.addCookie(cookie);
        return ApiResponse.<AuthResponse>builder().result(result).build();
    }
}
