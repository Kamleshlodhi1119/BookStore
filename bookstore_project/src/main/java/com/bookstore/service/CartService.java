package com.bookstore.service;

import com.bookstore.dto.CartItemDto;
import com.bookstore.dto.response.CartResponseDto;

public interface CartService {

    void addToCart(CartItemDto dto);

    CartResponseDto getMyCart();

    void updateQuantity(CartItemDto dto);

    void removeItem(Long bookId);

    void clearCart();
}
