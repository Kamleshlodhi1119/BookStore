package com.bookstore.service.impl;

import com.bookstore.entity.Book;
import com.bookstore.entity.User;
import com.bookstore.entity.Wishlist;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.repository.WishlistRepository;
import com.bookstore.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

	private final WishlistRepository wishlistRepo;
	private final BookRepository bookRepo;
	private final UserRepository userRepo;

	public WishlistServiceImpl(WishlistRepository wishlistRepo, BookRepository bookRepo, UserRepository userRepo) {
		super();
		this.wishlistRepo = wishlistRepo;
		this.bookRepo = bookRepo;
		this.userRepo = userRepo;
	}

	private User currentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public Wishlist add(Long bookId) {

		User user = currentUser();
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		return wishlistRepo.findByUserAndBook(user, book).orElseGet(() -> {
			Wishlist w = new Wishlist();
			w.setUser(user);
			w.setBook(book);
			return wishlistRepo.save(w);
		});
	}

	@Override
	public List<Wishlist> getMyWishlist() {
		return wishlistRepo.findByUser(currentUser());
	}

	@Override
	public void remove(Long bookId) {

		User user = currentUser();
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		Wishlist wishlist = wishlistRepo.findByUserAndBook(user, book)
				.orElseThrow(() -> new ResourceNotFoundException("Item not in wishlist"));

		wishlistRepo.delete(wishlist);
	}
}
