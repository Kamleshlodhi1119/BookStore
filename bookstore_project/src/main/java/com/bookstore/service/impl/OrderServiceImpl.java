package com.bookstore.service.impl;

import com.bookstore.dto.OrderItemResponseDto;
import com.bookstore.dto.OrderResponseDto;
import com.bookstore.entity.*;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.CartRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final UserRepository userRepo;

    public OrderServiceImpl(
            OrderRepository orderRepo,
            CartRepository cartRepo,
            UserRepository userRepo) {

        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // ---------------- PLACE ORDER ----------------

    @Override
    public Order placeOrder() {

        User user = currentUser();

        Cart cart = cartRepo.findByUserWithItems(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart has no items");
        }


        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");

        double total = 0;

        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setBook(ci.getBook());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtPurchase(ci.getBook().getPrice());

            order.getItems().add(oi);
            total += ci.getBook().getPrice() * ci.getQuantity();
        }

        order.setTotalAmount(total);

        Order savedOrder = orderRepo.save(order);

        // clear cart
        cart.getItems().clear();

        return savedOrder;
    }

    // ---------------- MY ORDERS ----------------
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Order> getMyOrders() {
//        return orderRepo.findByUserWithItems(currentUser());
//    }

    // ---------------- SINGLE ORDER ----------------

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(currentUser().getId())) {
            throw new SecurityException("Access denied");
        }

        return order;
    }

    // ---------------- ADMIN ----------------

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);
        orderRepo.save(order);
    }
    
    
    
    @Override
    public List<OrderResponseDto> getMyOrders() {

        User user = currentUser();

        return orderRepo.findByUserWithItems(user)
                .stream()
                .map(order -> {

                    OrderResponseDto dto = new OrderResponseDto();
                    dto.setId(order.getId());
                    dto.setStatus(order.getStatus());
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setCreatedAt(order.getCreatedAt());

                    dto.setItems(
                        order.getItems().stream().map(oi -> {
                            OrderItemResponseDto i = new OrderItemResponseDto();
                            i.setBookId(oi.getBook().getId());
                            i.setTitle(oi.getBook().getTitle());
                            i.setImageUrl(oi.getBook().getImageUrl());
                            i.setQuantity(oi.getQuantity());
                            i.setPrice(oi.getPriceAtPurchase());
                            return i;
                        }).toList()
                    );

                    return dto;
                })
                .toList();
    }
    
}
