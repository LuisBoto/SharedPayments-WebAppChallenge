package com.sharedPayments;

import java.util.List;
import java.util.stream.Collectors;

import com.sharedPayments.model.Payment;
import com.sharedPayments.model.PaymentEntity;
import com.sharedPayments.ports.PaymentRepository;
import com.sharedPayments.repository.PaymentInfraJpaRepository;
import com.sharedPayments.repository.UserInfraJpaRepository;

import jakarta.inject.Singleton;

@Singleton
public class PaymentRepositoryAdapter implements PaymentRepository {
	
	private PaymentInfraJpaRepository paymentJpa;
	private UserInfraJpaRepository userJpa;
	
	public PaymentRepositoryAdapter(PaymentInfraJpaRepository paymentRepository, UserInfraJpaRepository userRepository) {
		this.paymentJpa = paymentRepository;
		this.userJpa = userRepository;
	}

	@Override
	public Payment save(Payment payment) {
		var paymentE = PaymentEntity.fromModel(payment);
		var user = this.userJpa.findById(paymentE.getPayer().getId());
		paymentE.setPayer(user.get());
		return this.paymentJpa.save(paymentE).toModel();
	}

	@Override
	public Payment findById(Long id) {
		return this.paymentJpa.findById(id).get().toModel();
	}

	@Override
	public List<Payment> findAll() {
		return this.paymentJpa.findAll().stream().map(entity -> entity.toModel()).collect(Collectors.toList());
	}

	@Override
	public Payment update(Payment payment) {
		return this.paymentJpa.update(PaymentEntity.fromModel(payment)).toModel();
	}

}
