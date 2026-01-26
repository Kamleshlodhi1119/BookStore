package com.bookstore.controller;

import com.bookstore.dto.response.OrderResponseDto;
import com.bookstore.entity.Order;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
//@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}

	@PostMapping("/place")
	public Order place() {
		return orderService.placeOrder();
	}

	@GetMapping("/my")
	public List<OrderResponseDto> myOrders() {
	    return orderService.getMyOrders();
	}


	@GetMapping("/{id}")
	public Order view(@PathVariable Long id) {
		return orderService.getOrderById(id);
	}
}
