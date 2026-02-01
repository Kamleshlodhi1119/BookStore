package com.bookstore.service;

import java.util.List;

import com.bookstore.dto.RegisterRequest;
import com.bookstore.dto.UpdateProfileRequest;
import com.bookstore.entity.RoleType;
import com.bookstore.entity.User;

public interface UserService {
	User findByEmail(String email);

	Object register(RegisterRequest request);

	List<User> getAllUsers();

	User updateProfile(UpdateProfileRequest request);

	void changeUserRole(Long userId, String newRole);

	User getCurrentUser();

	User getUserById(Long id);

	void deleteUser(Long id);

	void changePassword(String currentPassword, String newPassword, String confirmPassword);
	void forgotPassword(String email);
	void resetPassword(String token, String newPassword, String confirmPassword);

}
