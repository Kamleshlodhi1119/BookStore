package com.bookstore.service;

import com.bookstore.entity.Payment;

public interface PaymentService {

	Payment initiatePayment(Long orderId);

	Payment verifyPayment(String paymentId, boolean success);
}
