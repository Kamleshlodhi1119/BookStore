package com.bookstore.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.bookstore.dto.BookDto;
import com.bookstore.entity.Book;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@RestController
@RequestMapping("/api/books")
//@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		super();
		this.bookService = bookService;
	}

	// ADMIN
	@PostMapping
	public BookDto createBook(@RequestBody BookDto dto) {
		return bookService.createBook(dto);
	}

	// ADMIN
	@PutMapping("/{id}")
	public BookDto updateBook(@PathVariable Long id, @RequestBody BookDto dto) {
		return bookService.updateBook(id, dto);
	}

	// PUBLIC
	@GetMapping("/{id}")
	public BookDto getBook(@PathVariable Long id) {
		return bookService.getBookById(id);
	}

	// PUBLIC
	@GetMapping
	public List<BookDto> getAllBooks() {
		return bookService.getAllBooks();
	}

	@GetMapping("/byadmin")
	public List<BookDto> getAllBooksByAdmin() {
		return bookService.getAllBooksByAdmin();
	}
	
	// ADMIN
	@DeleteMapping("/{id}")
	public void deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
	}

	@GetMapping("/search")
	public List<BookDto> search(@RequestParam String q) {
		return bookService.searchBooks(q);
	}

	@GetMapping("/by-author/{authorId}")
	public List<BookDto> byAuthor(@PathVariable Long authorId) {
		return bookService.getBooksByAuthor(authorId);
	}

	@GetMapping("/top-rated")
	public List<BookDto> topRated() {
		return bookService.getTopRatedBooks();
	}

	@GetMapping("/latest")
	public List<BookDto> latest() {
		return bookService.getLatestBooks();
	}
	
	
	
	@GetMapping(
	        value = "/{id}/image",
	        produces = org.springframework.http.MediaType.IMAGE_PNG_VALUE
	)
	public byte[] getBookImage(@PathVariable Long id) throws IOException {

	    Path imagePath = Paths.get("uploads/books", id + ".png");

	    if (!Files.exists(imagePath)) {
	        throw new ResourceNotFoundException("Image not found");
	    }

	    return Files.readAllBytes(imagePath);
	}

	
	@GetMapping("/filter")
	public List<BookDto> filterBooks(
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String author,
	        @RequestParam(required = false) Double maxPrice
	) {
	    return bookService.filterBooks(name, author, maxPrice);
	}


}
