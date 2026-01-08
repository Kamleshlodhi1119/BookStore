package com.bookstore.controller;

import com.bookstore.dto.PaymentRequestDto;
import com.bookstore.dto.PaymentVerifyDto;
import com.bookstore.entity.Payment;
import com.bookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

//	public PaymentController(PaymentService paymentService) {
//		super();
//		this.paymentService = paymentService;
//	}

	// POST /api/payments/initiate
	@PostMapping("/initiate")
	public Payment initiate(@RequestBody PaymentRequestDto dto) {
		return paymentService.initiatePayment(dto.getOrderId());
	}

	// POST /api/payments/verify
	@PostMapping("/verify")
	public Payment verify(@RequestBody PaymentVerifyDto dto) {
		return paymentService.verifyPayment(dto.getPaymentId(), dto.isSuccess());
	}
}
