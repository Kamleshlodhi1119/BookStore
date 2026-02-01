package com.bookstore.entity;


import jakarta.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "user_profiles")
public class UserProfile {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@OneToOne(optional = false)
@JoinColumn(name = "user_id", unique = true)
private User user;


private String fullName;
private String phone;
private String address;
private String city;
private String country;
private String avatarUrl;


private Instant createdAt;
private Instant updatedAt;


@PrePersist
void onCreate() {
createdAt = Instant.now();
updatedAt = createdAt;
}


@PreUpdate
void onUpdate() {
updatedAt = Instant.now();
}


public Long getId() {
return id;
}


public User getUser() {
return user;
}


public String getFullName() {
return fullName;
}


public String getPhone() {
return phone;
}


public String getAddress() {
return address;
}


public String getCity() {
return city;
}


public String getCountry() {
return country;
}


public String getAvatarUrl() {
return avatarUrl;
}


public void setUser(User user) {
this.user = user;
}


public void setFullName(String fullName) {
this.fullName = fullName;
}


public void setPhone(String phone) {
this.phone = phone;
}


public void setAddress(String address) {
this.address = address;
}


public void setCity(String city) {
this.city = city;
}


public void setCountry(String country) {
this.country = country;
}


public void setAvatarUrl(String avatarUrl) {
this.avatarUrl = avatarUrl;
}
}