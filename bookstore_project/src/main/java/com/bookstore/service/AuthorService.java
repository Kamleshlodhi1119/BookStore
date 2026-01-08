package com.bookstore.service;

import com.bookstore.entity.Author;

import java.util.List;

public interface AuthorService {

	Author createAuthor(String name);

	Author updateAuthor(Long id, String name);

	List<Author> getAllAuthors();

	Author getAuthorById(Long id);

	void deleteAuthor(Long id);
}
