package com.bookstore.service;


import com.bookstore.dto.UpdateProfileDetailsRequest;
import com.bookstore.dto.UserProfileDto;


public interface UserProfileService {


UserProfileDto getMyProfile();


UserProfileDto updateMyProfile(UpdateProfileDetailsRequest request);


void updateAvatar(String avatarUrl);
}