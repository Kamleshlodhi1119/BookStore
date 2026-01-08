package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConfigController {

	@Value("${spring.application.name:bookstore}")
	private String appName;

	// GET /api/config
	@GetMapping("/api/config")
	public Map<String, String> config() {
		return Map.of("application", appName, "version", "1.0.0", "environment", "local");
	}
}
