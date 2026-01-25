package com.bookstore.repository;

import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("""
        select distinct c from Cart c
        left join fetch c.items i
        left join fetch i.book
        where c.user = :user
    """)
    Optional<Cart> findByUserWithItems(@Param("user") User user);

    Optional<Cart> findByUser(User user);
}
