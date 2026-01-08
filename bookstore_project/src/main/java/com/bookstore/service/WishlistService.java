package com.bookstore.service;

import com.bookstore.entity.Wishlist;

import java.util.List;

public interface WishlistService {

	Wishlist add(Long bookId);

	List<Wishlist> getMyWishlist();

	void remove(Long bookId);
}
