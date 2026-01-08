package com.bookstore.repository;

import com.bookstore.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    // Check if user already rated this book
    boolean existsByBookIdAndUsername(Long bookId, String username);
    
    Optional<Rating> findByBookIdAndUsername(Long bookId, String username);
    List<Rating> findByBookId(Long bookId);
}