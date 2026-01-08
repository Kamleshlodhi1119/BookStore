package com.bookstore.controller;

import com.bookstore.entity.RoleType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

	// GET /api/roles
	@GetMapping
	public List<String> getAllRoles() {
		return List.of(RoleType.USER, RoleType.ADMIN);
	}
}
