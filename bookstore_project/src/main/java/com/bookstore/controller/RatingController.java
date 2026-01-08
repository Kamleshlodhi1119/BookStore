package com.bookstore.controller;

import com.bookstore.dto.RatingDto;
import com.bookstore.entity.Rating;
import com.bookstore.repository.RatingRepository;
import com.bookstore.service.RatingService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final RatingRepository ratingRepository;

   
    
//    public RatingController(RatingService ratingService, RatingRepository ratingRepository) {
//		super();
//		this.ratingService = ratingService;
//		this.ratingRepository = ratingRepository;
//	}

	@PostMapping("/{bookId}/rating")
    public ResponseEntity<?> addRating(@PathVariable Long bookId, @RequestBody RatingDto dto) {
        ratingService.addRating(bookId, dto.getRating(), dto.getComment(), dto.getUsername());
        
        // Check if we want to return a more specific message
        return ResponseEntity.ok(java.util.Map.of("message", "Rating saved successfully"));
    }
    
    @GetMapping("/{bookId}/ratings")
    public ResponseEntity<List<Rating>> getRatings(@PathVariable Long bookId) {
        return ResponseEntity.ok(ratingService.getRatingsByBookId(bookId));
    }
    
 // RatingController.java

    @DeleteMapping("/{bookId}/rating")
    public ResponseEntity<?> deleteRating(@PathVariable Long bookId) {
        // Get the email from the Security Token
        String authenticatedEmail = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // Call the service
        ratingService.deleteRating(bookId, authenticatedEmail);

        return ResponseEntity.ok(java.util.Map.of("message", "Review deleted successfully"));
    }
    
}
