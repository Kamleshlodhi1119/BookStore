package com.bookstore.dto;

import java.time.LocalDate;

public class RatingDto {

	private Integer rating;
	private String comment;
	private String username;
	private LocalDate ratedDate;

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDate getRatedDate() {
		return ratedDate;
	}

	public void setRatedDate(LocalDate ratedDate) {
		this.ratedDate = ratedDate;
	}
}
