package com.bookstore.controller;

import com.bookstore.dto.UpdateProfileRequest;
import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	// GET /api/users/me
	@GetMapping("/me")
	public User getMyProfile() {
		return userService.getCurrentUser();
	}

	// PUT /api/users/me
	@PutMapping("/me")
	public User updateMyProfile(@RequestBody UpdateProfileRequest request) {
		return userService.updateProfile(request);
	}
}
