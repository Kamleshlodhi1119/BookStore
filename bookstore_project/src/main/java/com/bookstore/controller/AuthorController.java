package com.bookstore.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.entity.Author;
import com.bookstore.service.AuthorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/authors")
//@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    
    

    public AuthorController(AuthorService authorService) {
		super();
		this.authorService = authorService;
	}



	@GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }
}
