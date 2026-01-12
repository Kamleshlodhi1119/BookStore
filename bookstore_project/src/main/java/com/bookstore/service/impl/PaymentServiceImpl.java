package com.bookstore.service.impl;

import com.bookstore.entity.Order;
import com.bookstore.entity.Payment;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.PaymentRepository;
import com.bookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
//@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepo;
	private final OrderRepository orderRepo;

	public PaymentServiceImpl(PaymentRepository paymentRepo, OrderRepository orderRepo) {
		super();
		this.paymentRepo = paymentRepo;
		this.orderRepo = orderRepo;
	}

	@Override
	public Payment initiatePayment(Long orderId) {

		Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		Payment payment = new Payment();
		payment.setOrderId(orderId);
		payment.setAmount(order.getTotalAmount());
		payment.setStatus("INITIATED");
		payment.setPaymentId(UUID.randomUUID().toString());

		return paymentRepo.save(payment);
	}

	@Override
	public Payment verifyPayment(String paymentId, boolean success) {

		Payment payment = paymentRepo.findByPaymentId(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

		payment.setStatus(success ? "SUCCESS" : "FAILED");
		return paymentRepo.save(payment);
	}
}
