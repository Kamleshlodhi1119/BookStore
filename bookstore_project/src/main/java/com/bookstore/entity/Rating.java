package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ratings")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer rating;
	private String comment;
	private String username;

	private LocalDate ratedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	@JsonIgnore 
	private Book book;

	public Rating() {
	}

	@PrePersist
	void onRate() {
		ratedDate = LocalDate.now();
	}

	// GETTERS & SETTERS
	public Long getId() {
		return id;
	}

	public Integer getRating() {
		return rating;
	}

	public String getComment() {
		return comment;
	}

	public String getUsername() {
		return username;
	}

	public LocalDate getRatedDate() {
		return ratedDate;
	}

	public Book getBook() {
		return book;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRatedDate(LocalDate ratedDate) {
		this.ratedDate = ratedDate;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
