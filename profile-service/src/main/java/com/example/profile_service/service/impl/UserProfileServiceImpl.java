package com.example.profile_service.service.impl;

import com.example.profile_service.dto.request.UserProfileCreationRequest;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.UserProfile;
import com.example.profile_service.mappper.UserProfileMapper;
import com.example.profile_service.repository.UserProfileRepository;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse createUserProfile(UserProfileCreationRequest request){
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile
                .setAvatar("https://i.pinimg.com/736x/6e/59/95/6e599501252c23bcf02658617b29c894.jpg");
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public List<UserProfileResponse> getAllProfile(){
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
}
