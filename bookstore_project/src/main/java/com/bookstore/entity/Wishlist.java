package com.bookstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "book_id" }))
public class Wishlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@ManyToOne
	private Book book;

	public Wishlist() {
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Book getBook() {
		return book;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
