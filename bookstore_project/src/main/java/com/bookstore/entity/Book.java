package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String isbn;

	private String title;
	private String description;
	private Double price;
	private Integer stockQuantity;

	private LocalDate publishDate = LocalDate.of(1900, 1, 1);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	@JsonIgnore
	private Author author;
	
	

//	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Rating> ratings = new ArrayList<>();
	
	// Inside Book.java
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Rating> ratings = new ArrayList<>();

	// Add this calculated method so the average is always accurate
	public Double getAverageRating() {
	    if (ratings == null || ratings.isEmpty()) {
	        return 0.0;
	    }
	    return ratings.stream()
	                  .mapToDouble(Rating::getRating)
	                  .average()
	                  .orElse(0.0);
	}


	private Instant createdAt;
	private Instant updatedAt;

	@Column(nullable = false)
	private boolean active = true;
	
	@Column(name = "image_url")
	private String imageUrl;

	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


//	public Book() {
//	}

	@PrePersist
	void onCreate() {
		createdAt = Instant.now();
		updatedAt = createdAt;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}

	// GETTERS & SETTERS
	public Long getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Double getPrice() {
		return price;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public LocalDate getPublishDate() {
		return publishDate;
	}

	public boolean getActive() {
		return active;
	}

	public Author getAuthor() {
		return author;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public void setActive(boolean active) {
		// TODO Auto-generated method stub
		this.active = active;

	}
}
