package com.bookstore.service.impl;

import com.bookstore.entity.*;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.*;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepo;
	private final CartRepository cartRepo;
	private final UserRepository userRepo;

	public OrderServiceImpl(OrderRepository orderRepo, CartRepository cartRepo, UserRepository userRepo) {
		super();
		this.orderRepo = orderRepo;
		this.cartRepo = cartRepo;
		this.userRepo = userRepo;
	}

	private User currentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public Order placeOrder() {

		User user = currentUser();
		Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

		if (cart.getItems().isEmpty()) {
			throw new IllegalStateException("Cart is empty");
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
			total += ci.getBook().getPrice() * ci.getQuantity();
			order.getItems().add(oi);
		}

		order.setTotalAmount(total);
		Order saved = orderRepo.save(order);

		cart.getItems().clear();
		cartRepo.save(cart);

		return saved;
	}

	@Override
	public List<Order> getMyOrders() {
		return orderRepo.findByUser(currentUser());
	}

	@Override
	public Order getOrderById(Long orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (!order.getUser().getId().equals(currentUser().getId())) {
			throw new SecurityException("Access denied");
		}
		return order;
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}

	@Override
	public void updateOrderStatus(Long orderId, String status) {
		Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		order.setStatus(status);
		orderRepo.save(order);
	}
}
