package com.example.identity_service.service.Impl;

import java.util.HashSet;
import java.util.List;

import com.example.identity_service.dto.request.ProfileCreationRequest;
import com.example.identity_service.dto.response.ProfileClientResponse;
import com.example.identity_service.dto.response.UserProfileResponse;
import com.example.identity_service.mapper.ProfileMapper;
import com.example.identity_service.repository.httpclient.ProfileClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.identity_service.dto.request.UserRequestDTO;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.enums.Role;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.UserRepository;
import com.example.identity_service.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    ProfileClient profileClient;
    ProfileMapper profileMapper;

    @Override
    public UserProfileResponse createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(userRequestDTO);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.STUDENT.name());

        String role = Role.STUDENT.name();
        user.setRoles(role);

        user = userRepository.save(user);

        var profileRequest = profileMapper.toProfileCreationRequest(userRequestDTO);
        profileRequest.setUserId(user.getUserId());

        return profileClient.createUserProfile(profileRequest).getResult();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {

        return userMapper.toUsersResponse(userRepository.findAll());
    }

    @Override
    public UserResponse getUser(int userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found")));
    }

    @Override
    public UserResponse updateUser(int userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));

        userMapper.updateUser(user, userUpdateRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserProfileResponse getInfoUserIndex() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        ProfileClientResponse response = profileClient.getUserProfileByUsername(username)
                .getResult();

        UserProfileResponse userProfileResponse
                = profileMapper.toUserProfileResponse(response);

        userProfileResponse.setRole(user.getRoles());

        return userProfileResponse;
    }
}
