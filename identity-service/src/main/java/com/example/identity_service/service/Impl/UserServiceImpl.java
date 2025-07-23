package com.example.identity_service.service.Impl;

import com.example.identity_service.dto.request.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.identity_service.dto.response.*;
import com.example.identity_service.mapper.ProfileMapper;
import com.example.identity_service.repository.httpclient.ProfileClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    PasswordEncoder passwordEncoder;

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

    @Override
    public UserPagingResponse<UserDetailsResponse> getPageUsersProfile(int cursor, Pageable pageable) {
        List<User> users = userRepository.findNextPage(cursor, pageable);
        List<String> listUsername = users.stream()
                .map(User::getUsername).toList();
        ListUsernameRequest listUsernameRequest = new ListUsernameRequest(listUsername);

        List<ProfileClientResponse> profileClientResponses = profileClient.getListUserProfileByUsername(listUsernameRequest).getResult();

        Map<String, String> usernameToRole = users.stream()
                .collect(
                        Collectors.toMap(
                                User::getUsername, User::getRoles));

        List<UserDetailsResponse> userResponses = profileClientResponses.stream()
                .map(profileClientResponse -> new UserDetailsResponse(
                        profileClientResponse.getUserId(),
                        profileClientResponse.getAvatar(),
                        profileClientResponse.getUsername(),
                        profileClientResponse.getEmail(),
                        profileClientResponse.getFullName(),
                        profileClientResponse.getDob(),
                        usernameToRole.get(profileClientResponse.getUsername())
                )).toList();

        boolean hasNext = users.size() == pageable.getPageSize();
        int lastCursor = users.isEmpty() ? cursor : users.getLast().getUserId();

        return new UserPagingResponse<>(userResponses, lastCursor, hasNext);
    }

    @Override
    public AccountResponse createNewUser(AccountRequest accountRequest) {
        Optional<User> userOptional = userRepository.findByUsername(accountRequest.getUsername());
        if (userOptional.isPresent()){
            throw new DuplicateKeyException("username already exist");
        }
        User user = new User();
        user.setUsername(accountRequest.getUsername());
        user.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        user.setRoles(accountRequest.getRoles());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("can not create new user");
        }

        return new AccountResponse(user.getUserId(), user.getUsername());
    }

    @Override
    public boolean deleteUserAccountByUserId(int userId) {
        Optional<User> userOptional = userRepository.getUserProfileByUserId(userId);
        if (userOptional.isPresent()) {
            try {
                userRepository.delete(userOptional.get());
            } catch (RuntimeException e) {
                throw new RuntimeException("Can not delete userProfile " + e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAccount(AccountUpdateRequest accountUpdateRequest) {
        Optional<User> userOptional = userRepository.getUserProfileByUserId(accountUpdateRequest.getUserId());
        if (userOptional.isPresent()) {
            try {
                User user = userOptional.get();
                user.setUsername(accountUpdateRequest.getUsername());
                user.setRoles(accountUpdateRequest.getRoles());
                userRepository.save(user);
            } catch (RuntimeException e) {
                throw new RuntimeException("Can not update userProfile " + e);
            }
            return true;
        }
        return false;
    }
}