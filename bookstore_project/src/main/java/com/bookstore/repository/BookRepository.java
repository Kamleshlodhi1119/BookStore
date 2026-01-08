package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByActiveTrue();

	List<Book> findByTitleContainingIgnoreCaseAndActiveTrue(String title);

	List<Book> findByAuthor_IdAndActiveTrue(Long authorId);

	List<Book> findTop5ByActiveTrueOrderByCreatedAtDesc();
}
