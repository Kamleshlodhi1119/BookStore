package com.bookstore.dto;

import java.time.Instant;
import java.util.List;

public class OrderResponseDto {
    private Long id;
    private String status;
    private double totalAmount;
    private Instant createdAt;
    private List<OrderItemResponseDto> items;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public List<OrderItemResponseDto> getItems() {
		return items;
	}
	public void setItems(List<OrderItemResponseDto> items) {
		this.items = items;
	}

    // getters & setters
}
