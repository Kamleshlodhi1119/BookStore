package com.bookstore.dto.response;


import java.time.Instant;
import java.util.List;

import com.bookstore.dto.AdminOrderItemDto;

public class AdminOrderResponseDto {

    private Long id;
    private String status;
    private double totalAmount;
    private Instant createdAt;
    private String userEmail;
    private List<AdminOrderItemDto> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<AdminOrderItemDto> getItems() { return items; }
    public void setItems(List<AdminOrderItemDto> items) { this.items = items; }
}
