package com.sharedPayments.ports;

import java.util.List;

import com.sharedPayments.model.Payment;

public interface PaymentRepository {
	
	public Payment save(Payment payment);
	
	public Payment findById(Long id);
	
	public List<Payment> findAll();
	
	public Payment update(Payment payment);

}
