package com.bookstore.controller;

import com.bookstore.entity.Order;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
//@RequiredArgsConstructor
public class AdminOrderController {

	private final OrderService orderService;

	public AdminOrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}

	@GetMapping
	public List<Order> allOrders() {
		return orderService.getAllOrders();
	}

	@PutMapping("/{id}/status")
	public String updateStatus(@PathVariable Long id, @RequestParam String status) {
		orderService.updateOrderStatus(id, status);
		return "Order status updated";
	}
}
