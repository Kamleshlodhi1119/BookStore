package com.bookstore.service.impl;

import com.bookstore.dto.AdminOrderItemDto;
import com.bookstore.dto.response.AdminOrderResponseDto;
import com.bookstore.dto.response.OrderItemResponseDto;
import com.bookstore.dto.response.OrderResponseDto;
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

    // ---------------- AUTH ----------------

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

        cart.getItems().clear();

        return savedOrder;
    }

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

    // ---------------- USER ORDERS ----------------

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders() {

        User user = currentUser();

        return orderRepo.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(o -> {
                    OrderResponseDto dto = new OrderResponseDto();
                    dto.setId(o.getId());
                    dto.setStatus(o.getStatus());
                    dto.setTotalAmount(o.getTotalAmount());
                    dto.setCreatedAt(o.getCreatedAt());

                    List<OrderItemResponseDto> items = o.getItems()
                            .stream()
                            .map(i -> {
                                OrderItemResponseDto item = new OrderItemResponseDto();
                                item.setBookId(i.getBook().getId());
                                item.setTitle(i.getBook().getTitle());
                                item.setQuantity(i.getQuantity());
                                item.setPrice(i.getPriceAtPurchase());
                                return item;
                            })
                            .toList();

                    dto.setItems(items);
                    return dto;
                })
                .toList();
    }

    // ---------------- ADMIN ORDERS ----------------

    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderResponseDto> getAllOrdersForAdmin() {

        return orderRepo.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(o -> {
                    AdminOrderResponseDto dto = new AdminOrderResponseDto();
                    dto.setId(o.getId());
                    dto.setStatus(o.getStatus());
                    dto.setTotalAmount(o.getTotalAmount());
                    dto.setCreatedAt(o.getCreatedAt());
                    dto.setUserEmail(o.getUser().getEmail());

                    List<AdminOrderItemDto> items = o.getItems()
                            .stream()
                            .map(i -> {
                                AdminOrderItemDto item = new AdminOrderItemDto();
                                item.setBookId(i.getBook().getId());
                                item.setTitle(i.getBook().getTitle());
                                item.setQuantity(i.getQuantity());
                                item.setPrice(i.getPriceAtPurchase());
                                return item;
                            })
                            .toList();

                    dto.setItems(items);
                    return dto;
                })
                .toList();
    }

    // ---------------- ADMIN UPDATE ----------------

    @Override
    public void updateOrderStatus(Long orderId, String status) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);
        orderRepo.save(order);
    }
}
