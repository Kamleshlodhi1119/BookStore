package com.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonBackReference
	private Order order;

	@ManyToOne
	private Book book;

	private int quantity;
	private double priceAtPurchase;

	public Long getId() {
		return id;
	}

	public Order getOrder() {
		return order;
	}

	public Book getBook() {
		return book;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPriceAtPurchase() {
		return priceAtPurchase;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setPriceAtPurchase(double priceAtPurchase) {
		this.priceAtPurchase = priceAtPurchase;
	}
}
