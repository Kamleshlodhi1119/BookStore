package com.bookstore.service;

import com.bookstore.dto.CartItemDto;
import com.bookstore.entity.Cart;

public interface CartService {

	Cart addToCart(CartItemDto dto);

	Cart getMyCart();

	Cart updateQuantity(CartItemDto dto);

	void removeItem(Long bookId);

	void clearCart();
}
