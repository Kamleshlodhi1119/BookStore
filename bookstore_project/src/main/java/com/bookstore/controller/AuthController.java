package com.bookstore.controller;

import com.bookstore.dto.LoginRequest;
import com.bookstore.dto.LoginResponse;
import com.bookstore.dto.RegisterRequest;
import com.bookstore.dto.UpdateProfileRequest;
import com.bookstore.entity.Author;
import com.bookstore.entity.RoleType;
import com.bookstore.entity.User;
import com.bookstore.security.JwtTokenProvider;
import com.bookstore.service.AuthorService;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://books-storeapp.netlify.app")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;

	private final AuthorService authorService;

//	public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
//			UserService userService, AuthorService authorService) {
//		super();
//		this.authenticationManager = authenticationManager;
//		this.jwtTokenProvider = jwtTokenProvider;
//		this.userService = userService;
//		this.authorService = authorService;
//	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		userService.register(request);
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		User user = userService.findByEmail(request.getEmail());

		// ✅ ROLE AS STRING
		String role = user.getRoles().stream().findFirst().orElse(RoleType.USER);

		String token = jwtTokenProvider.generateToken(user.getEmail(), role);

		return ResponseEntity.ok(new LoginResponse(token, role, "Login successful"));
	}

	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
		userService.updateProfile(request);
		return ResponseEntity.ok("Profile updated successfully");
	}
	
	
	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(Authentication authentication) {
	    if (authentication == null) {
	        return ResponseEntity.status(401).body("Not authenticated");
	    }
	    // authentication.getName() returns the email/username from the JWT token
	    User user = userService.findByEmail(authentication.getName());
	    return ResponseEntity.ok(user); 
	}

	@GetMapping
	public List<Author> getAllAuthors() {
		return authorService.getAllAuthors();
	}

	// GET /api/authors/{id}
	@GetMapping("/{id}")
	public Author getAuthor(@PathVariable Long id) {
		return authorService.getAuthorById(id);
	}
}
