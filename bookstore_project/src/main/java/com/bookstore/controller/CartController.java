package com.bookstore.controller;

import com.bookstore.dto.CartItemDto;
import com.bookstore.dto.response.CartResponseDto;
import com.bookstore.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public void add(@RequestBody CartItemDto dto) {
        cartService.addToCart(dto);
    }

    @GetMapping
    public CartResponseDto view() {
        return cartService.getMyCart();
    }

    @PutMapping("/update")
    public void update(@RequestBody CartItemDto dto) {
        cartService.updateQuantity(dto);
    }

    @DeleteMapping("/remove/{bookId}")
    public void remove(@PathVariable Long bookId) {
        cartService.removeItem(bookId);
    }

    @DeleteMapping("/clear")
    public void clear() {
        cartService.clearCart();
    }
}
