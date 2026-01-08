package com.bookstore.controller;

import com.bookstore.entity.RoleType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/roles")
public class AdminRolesController {

	// POST /api/admin/roles
	@PostMapping
	public String createRole(@RequestParam String role) {

		if (RoleType.USER.equals(role) || RoleType.ADMIN.equals(role)) {
			return "Role already exists";
		}

		// Since roles are constants, new roles are NOT supported
		return "Dynamic role creation is not supported";
	}
}
