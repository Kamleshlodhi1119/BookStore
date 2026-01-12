package com.bookstore.service.impl;

import com.bookstore.dto.CartItemDto;
import com.bookstore.entity.*;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.*;
import com.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    
    public CartServiceImpl(CartRepository cartRepo, CartItemRepository itemRepo, BookRepository bookRepo,
			UserRepository userRepo) {
		super();
		this.cartRepo = cartRepo;
		this.itemRepo = itemRepo;
		this.bookRepo = bookRepo;
		this.userRepo = userRepo;
	}

    
    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
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

    @Override
    public Cart addToCart(CartItemDto dto) {

        User user = currentUser();
        Cart cart = getOrCreateCart(user);

        Book book = bookRepo.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        for (CartItem item : cart.getItems()) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + dto.getQuantity());
                return cartRepo.save(cart);
            }
        }

        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(dto.getQuantity());
        item.setCart(cart);

        cart.getItems().add(item);
        return cartRepo.save(cart);
    }

    @Override
    public Cart getMyCart() {
        return cartRepo.findByUser(currentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Override
    public Cart updateQuantity(CartItemDto dto) {

        Cart cart = getMyCart();

        cart.getItems().forEach(item -> {
            if (item.getBook().getId().equals(dto.getBookId())) {
                item.setQuantity(dto.getQuantity());
            }
        });

        return cartRepo.save(cart);
    }

    @Override
    public void removeItem(Long bookId) {
        Cart cart = getMyCart();
        cart.getItems().removeIf(i -> i.getBook().getId().equals(bookId));
        cartRepo.save(cart);
    }

    @Override
    public void clearCart() {
        Cart cart = getMyCart();
        cart.getItems().clear();
        cartRepo.save(cart);
    }
}
