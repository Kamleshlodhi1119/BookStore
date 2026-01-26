package com.bookstore.service;

import com.bookstore.dto.response.WishlistResponseDto;
import com.bookstore.entity.Wishlist;

import java.util.List;

public interface WishlistService {



	void remove(Long bookId);
	
	List<WishlistResponseDto> getMyWishlist();
	WishlistResponseDto add(Long bookId);
}
