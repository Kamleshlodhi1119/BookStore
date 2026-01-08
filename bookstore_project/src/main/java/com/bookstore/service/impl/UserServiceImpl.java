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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository repo;
	private final PasswordEncoder encoder;

	public UserServiceImpl(UserRepository repo, PasswordEncoder encoder) {
		super();
		this.repo = repo;
		this.encoder = encoder;
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
}
