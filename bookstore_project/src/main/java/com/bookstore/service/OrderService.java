package com.bookstore.service;

import com.bookstore.dto.response.AdminOrderResponseDto;
import com.bookstore.dto.response.OrderResponseDto;
import com.bookstore.entity.Order;
import java.util.List;

public interface OrderService {

    Order placeOrder();

    List<OrderResponseDto> getMyOrders();
    List<AdminOrderResponseDto> getAllOrdersForAdmin();

    Order getOrderById(Long orderId);

    void updateOrderStatus(Long orderId, String status);
}
