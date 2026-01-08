package com.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
    @JsonIgnore
	private Cart cart;

	@ManyToOne
	private Book book;

	private int quantity;

	public CartItem() {
	}

	public Long getId() {
		return id;
	}

	public Cart getCart() {
		return cart;
	}

	public Book getBook() {
		return book;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
