package com.bookstore.service;

import java.util.List;

import com.bookstore.entity.Rating;

public interface RatingService {
    void addRating(Long bookId, Integer rating, String comment, String username);
    List<Rating> getRatingsByBookId(Long bookId);
 // RatingService.java
    void deleteRating(Long bookId, String username);
    
    
}
