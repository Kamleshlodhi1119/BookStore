package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String passwordHash;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserProfile profile;

	
	

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private Set<String> roles = new HashSet<>();

	private Instant createdAt;
	private Instant updatedAt;

	public User() {
	}

	// Helper
	public void addRole(String role) {
		this.roles.add(role);
	}

	// Builder (kept)
	public static class Builder {
		private String username;
		private String email;
		private String passwordHash;
		private Set<String> roles;

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder passwordHash(String passwordHash) {
			this.passwordHash = passwordHash;
			return this;
		}

		public Builder roles(Set<String> roles) {
			this.roles = roles;
			return this;
		}

		public User build() {
			User u = new User();
			u.username = this.username;
			u.email = this.email;
			u.passwordHash = this.passwordHash;
			u.roles = this.roles;
			return u;
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	// GETTERS
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public Set<String> getRoles() {
		return roles;
	}

	// SETTERS
	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@PrePersist
	void onCreate() {
		createdAt = Instant.now();
		updatedAt = createdAt;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}
}
