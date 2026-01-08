package com.bookstore.service.impl;

import com.bookstore.entity.Book;
import com.bookstore.entity.Rating;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.RatingRepository;
import com.bookstore.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional; // Add this line!

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(BookRepository bookRepository, RatingRepository ratingRepository) {
		super();
		this.bookRepository = bookRepository;
		this.ratingRepository = ratingRepository;
	}
    @Override
    public void addRating(Long bookId, Integer rating, String comment, String username) {
        // 1. Get real identity from Token
        String authenticatedEmail = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // 2. Look for existing rating
        Optional<Rating> existingRating = ratingRepository.findByBookIdAndUsername(bookId, authenticatedEmail);

        if (existingRating.isPresent()) {
            // UPDATE EXISTING
            Rating entity = existingRating.get();
            entity.setRating(rating);
            entity.setComment(comment);
            entity.setRatedDate(java.time.LocalDate.now()); // Update the date
            ratingRepository.save(entity);
        } else {
            // CREATE NEW
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            Rating ratingEntity = new Rating();
            ratingEntity.setRating(rating);
            ratingEntity.setComment(comment);
            ratingEntity.setUsername(authenticatedEmail);
            ratingEntity.setBook(book);

            ratingRepository.save(ratingEntity);
        }
    }
    
    @Override
    public List<Rating> getRatingsByBookId(Long bookId) {
        return ratingRepository.findByBookId(bookId);
    }
    
    
 // RatingServiceImpl.java

    @Override
    @Transactional
    public void deleteRating(Long bookId, String username) {
        // 1. Find the specific rating
        Rating rating = ratingRepository.findByBookIdAndUsername(bookId, username)
                .orElseThrow(() -> new RuntimeException("Review not found for this user on this book"));

        // 2. Delete it
        ratingRepository.delete(rating);
    }
}
