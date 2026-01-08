package com.bookstore.service.impl;

import com.bookstore.entity.Author;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;

	public AuthorServiceImpl(AuthorRepository authorRepository) {
		super();
		this.authorRepository = authorRepository;
	}

	@Override
	public Author createAuthor(String name) {
		Author author = new Author();
		author.setName(name);
		return authorRepository.save(author);
	}

	@Override
	public Author updateAuthor(Long id, String name) {
		Author author = getAuthorById(id);
		author.setName(name);
		return authorRepository.save(author);
	}

	@Override
	public List<Author> getAllAuthors() {
		return authorRepository.findAll();
	}

	@Override
	public Author getAuthorById(Long id) {
		return authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
	}

	@Override
	public void deleteAuthor(Long id) {
		if (!authorRepository.existsById(id)) {
			throw new ResourceNotFoundException("Author not found");
		}
		authorRepository.deleteById(id);
	}
}
