package com.sharedPayments.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import com.sharedPayments.dto.PaymentDto;
import com.sharedPayments.model.User;
import com.sharedPayments.ports.PaymentRepository;
import com.sharedPayments.ports.UserRepository;

import jakarta.inject.Singleton;

@Singleton
public class PaymentService {

	private PaymentRepository paymentRepository;
	private UserRepository userRepository;
	
	public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
		this.paymentRepository = paymentRepository;
		this.userRepository = userRepository;
	}
	
	private Comparator<PaymentDto> comparePaymentsByDate = ((PaymentDto o1, PaymentDto o2)-> 
			o1.getPaymentDate() <= o2.getPaymentDate() ?1:-1);

	public List<PaymentDto> getPayments() {
		List<PaymentDto> result = new ArrayList<PaymentDto>();
		paymentRepository.findAll().forEach(payment -> result.add(payment.toDto()));
		result.sort(this.comparePaymentsByDate);
		return result;
	}

	public PaymentDto createPayment(PaymentDto newPayment) {
		User payerUser = this.userRepository.findById(newPayment.getPayerId());
		if (payerUser == null)
			throw new NoSuchElementException("Payer User not found");

		var createdPayment = this.paymentRepository.save(newPayment.toModel(payerUser)).toDto();
		return createdPayment;
	}
	
}