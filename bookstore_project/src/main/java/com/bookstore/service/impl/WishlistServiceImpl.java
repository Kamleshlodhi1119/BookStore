package com.bookstore.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bookstore.dto.response.WishlistResponseDto;
import com.bookstore.entity.Book;
import com.bookstore.entity.User;
import com.bookstore.entity.Wishlist;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.repository.WishlistRepository;
import com.bookstore.service.WishlistService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepo,
            BookRepository bookRepo,
            UserRepository userRepo) {
        this.wishlistRepo = wishlistRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public WishlistResponseDto add(Long bookId) {

        User user = currentUser();
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Wishlist wishlist = wishlistRepo.findByUserAndBook(user, book)
                .orElseGet(() -> {
                    Wishlist w = new Wishlist();
                    w.setUser(user);
                    w.setBook(book);
                    return wishlistRepo.save(w);
                });

        return map(wishlist);
    }

    @Override
//    @Transactional(readOnly = true)
    public List<WishlistResponseDto> getMyWishlist() {

        return wishlistRepo.findByUser(currentUser())
                .stream()
                .map(this::map)
                .toList();
    }

    private WishlistResponseDto map(Wishlist w) {
        WishlistResponseDto dto = new WishlistResponseDto();
        dto.setId(w.getId());
        dto.setBookId(w.getBook().getId());
        dto.setTitle(w.getBook().getTitle());
        dto.setImageUrl(w.getBook().getImageUrl());
        dto.setPrice(w.getBook().getPrice());
        return dto;
    }

    @Override
    public void remove(Long bookId) {
        User user = currentUser();
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Wishlist wishlist = wishlistRepo.findByUserAndBook(user, book)
                .orElseThrow(() -> new ResourceNotFoundException("Item not in wishlist"));

        wishlistRepo.delete(wishlist);
    }
}
