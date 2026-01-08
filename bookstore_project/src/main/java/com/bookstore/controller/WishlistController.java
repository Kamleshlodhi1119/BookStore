package com.bookstore.controller;

import com.bookstore.entity.Wishlist;
import com.bookstore.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

	private final WishlistService wishlistService;

//	public WishlistController(WishlistService wishlistService) {
//		super();
//		this.wishlistService = wishlistService;
//	}

	@PostMapping("/add/{bookId}")
	public Wishlist add(@PathVariable Long bookId) {
		return wishlistService.add(bookId);
	}

	@GetMapping
	public List<Wishlist> getMyWishlist() {
		return wishlistService.getMyWishlist();
	}

	@DeleteMapping("/remove/{bookId}")
	public String remove(@PathVariable Long bookId) {
		wishlistService.remove(bookId);
		return "Removed from wishlist";
	}
}
