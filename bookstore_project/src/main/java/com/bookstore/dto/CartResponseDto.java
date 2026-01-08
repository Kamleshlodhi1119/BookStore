package com.bookstore.dto;

import java.util.List;

public class CartResponseDto {
    private List<CartItemResponseDto> items;
    private double totalAmount;
	public List<CartItemResponseDto> getItems() {
		return items;
	}
	public void setItems(List<CartItemResponseDto> items) {
		this.items = items;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

    // getters & setters
    
    
    
}

