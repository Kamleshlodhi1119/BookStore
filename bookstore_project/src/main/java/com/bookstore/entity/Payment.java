package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long orderId;
	private String paymentId;
	private String status; // INITIATED, SUCCESS, FAILED
	private double amount;

	private Instant createdAt;

	@PrePersist
	void onCreate() {
		createdAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public String getStatus() {
		return status;
	}

	public double getAmount() {
		return amount;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
