package com.bookstore.controller;

import com.bookstore.entity.Author;
import com.bookstore.entity.User;
import com.bookstore.service.AuthorService;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

	private final UserService userService;
	private final AuthorService authorService;

	public AdminUserController(UserService userService, AuthorService authorService) {
		super();
		this.userService = userService;
		this.authorService = authorService;
	}

	// GET /api/admin/users
	@GetMapping
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	// GET /api/admin/users/{id}
	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	// PUT /api/admin/users/{id}/role
	@PutMapping("/{id}/role")
	public String changeRole(@PathVariable Long id, @RequestParam String role) {
		userService.changeUserRole(id, role);
		return "Role updated";
	}

	// DELETE /api/admin/users/{id}
	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return "User deleted";
	}

	// POST /api/admin/authors
	@PostMapping
	public Author create(@RequestParam String name) {
		return authorService.createAuthor(name);
	}

	// PUT /api/admin/authors/{id}
	@PutMapping("/{id}")
	public Author update(@PathVariable Long id, @RequestParam String name) {
		return authorService.updateAuthor(id, name);
	}

	// DELETE /api/admin/authors/{id}
//	@DeleteMapping("/{id}")
//	public String delete(@PathVariable Long id) {
//		authorService.deleteAuthor(id);
//		return "Author deleted";
//	}
}
