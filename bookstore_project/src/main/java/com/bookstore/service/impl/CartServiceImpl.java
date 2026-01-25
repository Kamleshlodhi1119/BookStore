package com.bookstore.service.impl;

import com.bookstore.dto.CartItemDto;
import com.bookstore.dto.CartItemResponseDto;
import com.bookstore.dto.CartResponseDto;
import com.bookstore.entity.*;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.*;
import com.bookstore.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public CartServiceImpl(
            CartRepository cartRepo,
            BookRepository bookRepo,
            UserRepository userRepo) {

        this.cartRepo = cartRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        return userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepo.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepo.save(c);
        });
    }

    // ---------- READ (DTO ONLY) ----------

    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getMyCart() {

        Cart cart = cartRepo.findByUserWithItems(currentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        double total = 0;

        List<CartItemResponseDto> items = cart.getItems().stream().map(ci -> {
            CartItemResponseDto dto = new CartItemResponseDto();
            dto.setBookId(ci.getBook().getId());
            dto.setTitle(ci.getBook().getTitle());
            dto.setPrice(ci.getBook().getPrice());
            dto.setQuantity(ci.getQuantity());
            dto.setImageUrl(ci.getBook().getImageUrl());

            return dto;
        }).toList();

        for (CartItem ci : cart.getItems()) {
            total += ci.getBook().getPrice() * ci.getQuantity();
        }

        CartResponseDto response = new CartResponseDto();
        response.setItems(items);
        response.setTotalAmount(total);

        return response;
    }

    // ---------- WRITE OPERATIONS ----------

    @Override
    public void addToCart(CartItemDto dto) {

        User user = currentUser();
        Cart cart = getOrCreateCart(user);

        Book book = bookRepo.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        for (CartItem item : cart.getItems()) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + dto.getQuantity());
                return;
            }
        }

        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(dto.getQuantity());
        item.setCart(cart);
        cart.getItems().add(item);
    }

    @Override
    public void updateQuantity(CartItemDto dto) {

        Cart cart = getOrCreateCart(currentUser());

        cart.getItems().forEach(item -> {
            if (item.getBook().getId().equals(dto.getBookId())) {
                item.setQuantity(dto.getQuantity());
            }
        });
    }

    @Override
    public void removeItem(Long bookId) {
        Cart cart = getOrCreateCart(currentUser());
        cart.getItems().removeIf(i -> i.getBook().getId().equals(bookId));
    }

    @Override
    public void clearCart() {
        Cart cart = getOrCreateCart(currentUser());
        cart.getItems().clear();
    }
}
