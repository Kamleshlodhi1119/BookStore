package com.bookstore.service.impl;

import com.bookstore.dto.RegisterRequest;
import com.bookstore.dto.UpdateProfileRequest;
import com.bookstore.entity.RoleType;
import com.bookstore.entity.User;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import com.bookstore.entity.PasswordResetToken;
import com.bookstore.repository.PasswordResetTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository repo;
	private final PasswordEncoder encoder;
	private final PasswordResetTokenRepository tokenRepo;


	public UserServiceImpl(UserRepository repo,
            PasswordEncoder encoder,
            PasswordResetTokenRepository tokenRepo) {
this.repo = repo;
this.encoder = encoder;
this.tokenRepo = tokenRepo;
}


	@Override
	public User register(RegisterRequest request) {
		User user = User.builder().username(request.getUsername()).email(request.getEmail())
				.passwordHash(encoder.encode(request.getPassword())).roles(Set.of(RoleType.USER)).build();

		return repo.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return repo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public List<User> getAllUsers() {
		return repo.findAll();
	}

	@Override
	public User getUserById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public User getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return findByEmail(email);
	}

	@Override
	public User updateProfile(UpdateProfileRequest request) {
		User user = getCurrentUser();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		return repo.save(user);
	}

	@Override
	public void changeUserRole(Long userId, String newRole) {
		User user = getUserById(userId);
		user.getRoles().clear();
		user.getRoles().add(newRole);
		repo.save(user);
	}

	@Override
	public void deleteUser(Long id) {
		repo.deleteById(id);
	}
	
	@Override
	public void changePassword(String currentPassword,
	                           String newPassword,
	                           String confirmPassword) {

	    User user = getCurrentUser();

	    if (!encoder.matches(currentPassword, user.getPasswordHash())) {
	        throw new RuntimeException("Current password incorrect");
	    }

	    if (!newPassword.equals(confirmPassword)) {
	        throw new RuntimeException("Passwords do not match");
	    }

	    user.setPasswordHash(encoder.encode(newPassword));
	    repo.save(user);

	    tokenRepo.deleteByUserId(user.getId());
	}

	
	@Override
	public void forgotPassword(String email) {

	    User user = findByEmail(email);

	    tokenRepo.deleteByUserId(user.getId());

	    String token = UUID.randomUUID().toString();

	    PasswordResetToken resetToken = new PasswordResetToken();
	    resetToken.setToken(token);
	    resetToken.setUser(user);
	    resetToken.setExpiryTime(Instant.now().plusSeconds(900)); // 15 min

	    tokenRepo.save(resetToken);

	    // EMAIL INTEGRATION GOES HERE
	    System.out.println("RESET LINK:");
	    System.out.println("http://localhost:4200/reset-password?token=" + token);
	}

	
	@Override
	public void resetPassword(String token,
	                          String newPassword,
	                          String confirmPassword) {

	    PasswordResetToken resetToken = tokenRepo.findByToken(token)
	            .orElseThrow(() -> new RuntimeException("Invalid token"));

	    if (resetToken.getExpiryTime().isBefore(Instant.now())) {
	        throw new RuntimeException("Token expired");
	    }

	    if (!newPassword.equals(confirmPassword)) {
	        throw new RuntimeException("Passwords do not match");
	    }

	    User user = resetToken.getUser();
	    user.setPasswordHash(encoder.encode(newPassword));
	    repo.save(user);

	    tokenRepo.delete(resetToken);
	}

}
