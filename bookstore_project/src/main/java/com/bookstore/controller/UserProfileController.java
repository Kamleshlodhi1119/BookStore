package com.bookstore.controller;

import com.bookstore.dto.UpdateProfileDetailsRequest;
import com.bookstore.dto.UserProfileDto;
import com.bookstore.service.SupabaseStorageService;
import com.bookstore.service.UserProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService profileService;
    private final SupabaseStorageService storageService;
    

    public UserProfileController(UserProfileService profileService, SupabaseStorageService storageService) {
		super();
		this.profileService = profileService;
		this.storageService = storageService;
	}

	// ---------------- GET MY PROFILE ----------------
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    // ---------------- UPDATE PROFILE ----------------
    @PutMapping("/update")
    public ResponseEntity<UserProfileDto> updateProfile(
            @RequestBody UpdateProfileDetailsRequest request) {

        return ResponseEntity.ok(profileService.updateMyProfile(request));
    }

    // ---------------- UPLOAD AVATAR (FILE) ----------------
    @PostMapping("/avatar/upload")
    public ResponseEntity<String> uploadProfileAvatar(
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // MIME validation
        String contentType = file.getContentType();
        if (contentType == null ||
            !(contentType.equals("image/png") ||
              contentType.equals("image/jpeg") ||
              contentType.equals("image/webp"))) {
            return ResponseEntity.badRequest()
                    .body("Only PNG, JPG, JPEG, WEBP allowed");
        }

        // Size validation (2MB max)
        if (file.getSize() > 2 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                    .body("File too large (Max 2MB)");
        }

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        String publicUrl = storageService.uploadProfileAvatar(
                email.hashCode() * 1L,
                file
        );

        profileService.updateAvatar(publicUrl);

        return ResponseEntity.ok(publicUrl);
    }


    // ---------------- UPDATE AVATAR (URL) ----------------
    @PostMapping("/avatar")
    public ResponseEntity<String> updateAvatar(
            @RequestParam String avatarUrl) {

        profileService.updateAvatar(avatarUrl);
        return ResponseEntity.ok("Avatar updated");
    }
}
