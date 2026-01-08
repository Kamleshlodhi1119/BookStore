package com.bookstore.service;

import com.bookstore.dto.BookDto;

import java.util.List;

public interface BookService {

	BookDto createBook(BookDto dto);

	BookDto updateBook(Long id, BookDto dto);

	BookDto getBookById(Long id);

	List<BookDto> getAllBooks();
	List<BookDto> getAllBooksByAdmin();
	

	void deleteBook(Long id);

	void addRating(Long bookId, Integer rating, String comment, String username);

	List<BookDto> searchBooks(String keyword);

	List<BookDto> getBooksByAuthor(Long authorId);

	List<BookDto> getTopRatedBooks();

	List<BookDto> getLatestBooks();

	void updateBookStatus(Long bookId, boolean active);
	List<BookDto> filterBooks(String name, String author, Double maxPrice);


}
