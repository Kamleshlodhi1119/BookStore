package com.bookstore.dto;

public class CartItemDto {

	private Long bookId;
	private int quantity;

	public Long getBookId() {
		return bookId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
