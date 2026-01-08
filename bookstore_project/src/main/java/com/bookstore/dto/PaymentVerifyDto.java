package com.bookstore.dto;

public class PaymentVerifyDto {
	private String paymentId;
	private boolean success;

	public String getPaymentId() {
		return paymentId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
