package com.bookstore.service.impl;


import com.bookstore.dto.UpdateProfileDetailsRequest;
import com.bookstore.dto.UserProfileDto;
import com.bookstore.entity.User;
import com.bookstore.entity.UserProfile;
import com.bookstore.repository.UserProfileRepository;
import com.bookstore.service.UserProfileService;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
//@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {


private final UserProfileRepository profileRepo;
private final UserService userService;



public UserProfileServiceImpl(UserProfileRepository profileRepo, UserService userService) {
	super();
	this.profileRepo = profileRepo;
	this.userService = userService;
}


// ---------------- GET PROFILE ----------------
@Override
public UserProfileDto getMyProfile() {


User user = userService.getCurrentUser();


UserProfile profile = profileRepo.findByUser(user)
.orElseGet(() -> createEmptyProfile(user));


return toDto(profile);
}


// ---------------- UPDATE PROFILE ----------------
@Override
public UserProfileDto updateMyProfile(UpdateProfileDetailsRequest request) {


User user = userService.getCurrentUser();


UserProfile profile = profileRepo.findByUser(user)
.orElseGet(() -> createEmptyProfile(user));


profile.setFullName(request.getFullName());
profile.setPhone(request.getPhone());
profile.setAddress(request.getAddress());
profile.setCity(request.getCity());
profile.setCountry(request.getCountry());


profileRepo.save(profile);


return toDto(profile);
}


// ---------------- AVATAR UPDATE ----------------
@Override
public void updateAvatar(String avatarUrl) {


User user = userService.getCurrentUser();


UserProfile profile = profileRepo.findByUser(user)
.orElseGet(() -> createEmptyProfile(user));


profile.setAvatarUrl(avatarUrl);
profileRepo.save(profile);
}


// ---------------- HELPERS ----------------


private UserProfile createEmptyProfile(User user) {
UserProfile profile = new UserProfile();
profile.setUser(user);
return profileRepo.save(profile);
}


private UserProfileDto toDto(UserProfile profile) {


UserProfileDto dto = new UserProfileDto();


dto.setFullName(profile.getFullName());
dto.setPhone(profile.getPhone());
dto.setAddress(profile.getAddress());
dto.setCity(profile.getCity());
dto.setCountry(profile.getCountry());
dto.setAvatarUrl(profile.getAvatarUrl());


return dto;
}
}