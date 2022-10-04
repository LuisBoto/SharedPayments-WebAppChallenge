package com.sharedPayments;

import java.util.List;
import java.util.stream.Collectors;

import com.sharedPayments.model.Payment;
import com.sharedPayments.model.PaymentEntity;
import com.sharedPayments.ports.PaymentRepository;
import com.sharedPayments.repository.PaymentInfraJpaRepository;

import io.micronaut.context.annotation.Bean;

@Bean
public class PaymentRepositoryAdapter implements PaymentRepository {
	
	private PaymentInfraJpaRepository paymentJpa;
	
	public PaymentRepositoryAdapter(PaymentInfraJpaRepository repository) {
		this.paymentJpa = repository;
	}

	@Override
	public Payment save(Payment payment) {
		return this.paymentJpa.save(PaymentEntity.fromModel(payment)).toModel();
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
