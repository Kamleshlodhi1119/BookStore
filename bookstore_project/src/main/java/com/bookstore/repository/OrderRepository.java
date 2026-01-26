package com.bookstore.repository;

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        select distinct o from Order o
        left join fetch o.items i
        left join fetch i.book
        where o.user = :user
        order by o.createdAt desc
    """)
    List<Order> findByUserWithItems(@Param("user") User user);

    List<Order> findByUser(User user);
    
    @Query("""
    		  select distinct o from Order o
    		  left join fetch o.items i
    		  left join fetch i.book
    		  where o.user = :user
    		  order by o.createdAt desc
    		""")
    		List<Order> findOrdersWithItems(@Param("user") User user);

}
