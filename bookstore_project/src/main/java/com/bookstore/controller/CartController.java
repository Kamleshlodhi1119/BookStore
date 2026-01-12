package com.bookstore.controller;

import com.bookstore.dto.CartItemDto;
import com.bookstore.entity.Cart;
import com.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
//@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		super();
		this.cartService = cartService;
	}

	@PostMapping("/add")
	public Cart add(@RequestBody CartItemDto dto) {
		return cartService.addToCart(dto);
	}


	@GetMapping("")
	public Cart view() {
		return cartService.getMyCart();
	}
	
	
	@PutMapping("/update")
	public Cart update(@RequestBody CartItemDto dto) {
		return cartService.updateQuantity(dto);
	}

	@DeleteMapping("/remove/{bookId}")
	public String remove(@PathVariable Long bookId) {
		cartService.removeItem(bookId);
		return "Item removed";
	}

	@DeleteMapping("/clear")
	public String clear() {
		cartService.clearCart();
		return "Cart cleared";
	}
}
