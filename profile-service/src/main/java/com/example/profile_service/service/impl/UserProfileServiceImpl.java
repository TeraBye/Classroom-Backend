package com.example.profile_service.service.impl;

import com.example.profile_service.dto.request.*;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.UserProfile;
import com.example.profile_service.dto.response.AccountResponse;
import com.example.profile_service.exception.BusinessException;
import com.example.profile_service.mappper.UserProfileMapper;
import com.example.profile_service.repository.UserProfileRepository;
import com.example.profile_service.repository.httpClient.IdentityClient;
import com.example.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    IdentityClient identityClient;

    @Override
    public UserProfileResponse createUserProfile(UserProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile
                .setAvatar("https://i.pinimg.com/736x/6e/59/95/6e599501252c23bcf02658617b29c894.jpg");
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public List<UserProfileResponse> getAllProfile() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        return userProfileMapper.toUserProfilesResponse(userProfiles);
    }

    @Override
    public UserProfileResponse getUserProfileByUsername(String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with username: " + username));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public void deleteUserProfile(String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with username: " + username));

        userProfileRepository.delete(userProfile);
    }

    @Override
    public List<UserProfileResponse> getUserProfileByListUsername(
            List<String> usernames) {
        return userProfileMapper.toUserProfilesResponse(
                userProfileRepository.findAllByUsernames(usernames));
    }

    @Override
    public Page<UserProfileResponse> searchUsers(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfile> userProfiles = userProfileRepository.searchUsers(q, pageable);
        return userProfiles.map(userProfileMapper::toUserProfileResponse);
    }

    @Override
    public boolean createUser(CreateUserRequest createUserRequest) {

        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUsername(createUserRequest.getUsername());
        if (userProfileOptional.isPresent()) {
            throw new BusinessException("User already exist.");
        }

        AccountRequest accountProfileRequest = new AccountRequest();
        accountProfileRequest.setUsername(createUserRequest.getUsername());
        accountProfileRequest.setPassword(createUserRequest.getPassword());
        accountProfileRequest.setRoles(createUserRequest.getRoles());

        UserProfile userProfile = new UserProfile();

        try {
            AccountResponse accountInfoResponse = identityClient.createUser(accountProfileRequest).getResult();

            userProfile.setUserId(accountInfoResponse.getUserId());
            userProfile.setUsername(accountInfoResponse.getUsername());
            userProfile.setFullName(createUserRequest.getFullName());
            userProfile.setEmail(createUserRequest.getEmail());
            userProfile.setDob(createUserRequest.getDob());
            userProfile.setAvatar("https://i.pinimg.com/736x/6e/59/95/6e599501252c23bcf02658617b29c894.jpg");
        } catch (Exception ex) {
            log.error("Feign call failed", ex);
            throw new RuntimeException("Feign call failed: " + ex.getMessage());
        }

        try {
            userProfileRepository.save(userProfile);
        } catch (RuntimeException e) {
            throw new RuntimeException("Can not create userProfile " + e);
        }
        return true;
    }

    @Override
    public String updateUser(UpdateUserRequest updateUserRequest) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserId(updateUserRequest.getUserId());
        if (userProfileOptional.isEmpty()) {
            return "can not find user";
        }
        UserProfile userProfile = userProfileOptional.get();


        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
        accountUpdateRequest.setUserId(userProfile.getUserId());
        accountUpdateRequest.setUsername(updateUserRequest.getUsername());
        accountUpdateRequest.setRoles(updateUserRequest.getRoles());

        if (identityClient.updateAccount(accountUpdateRequest).getCode() == 200){
            userProfile.setUsername(updateUserRequest.getUsername());
            userProfile.setFullName(updateUserRequest.getFullName());
            userProfile.setEmail(updateUserRequest.getEmail());
            userProfile.setDob(updateUserRequest.getDob());

            userProfileRepository.save(userProfile);
            return "";
        }
        return "update account failed!";
    }

    @Override
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        UserProfile userProfile = userProfileRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userProfile.setUsername(request.getUsername());
        userProfile.setFullName(request.getFullName());
        userProfile.setEmail(request.getEmail());
        userProfile.setDob(request.getDob());
        userProfile.setAvatar(request.getAvatar());

        UserProfile saved = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(saved);
    }
}