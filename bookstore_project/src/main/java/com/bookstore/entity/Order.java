package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	
	private List<OrderItem> items = new ArrayList<>();

	private String status; // PLACED, SHIPPED, DELIVERED, CANCELLED
	private double totalAmount;
	private Instant createdAt;

	@PrePersist
	void onCreate() {
		createdAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public String getStatus() {
		return status;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	public Instant getCreatedAt() {
	    return createdAt;
	}

}
