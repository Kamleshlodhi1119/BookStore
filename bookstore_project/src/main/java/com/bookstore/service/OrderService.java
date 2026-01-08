package com.bookstore.service;

import com.bookstore.entity.Order;

import java.util.List;

public interface OrderService {

	Order placeOrder();

	List<Order> getMyOrders();

	Order getOrderById(Long orderId);

	List<Order> getAllOrders();

	void updateOrderStatus(Long orderId, String status);
}
