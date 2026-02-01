package com.bookstore.repository;


import com.bookstore.entity.User;
import com.bookstore.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
Optional<UserProfile> findByUser(User user);
}