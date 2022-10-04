package com.sharedPayments.integration;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;

import com.sharedPayments.model.PaymentEntity;
import com.sharedPayments.model.UserEntity;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public abstract class GenericIT {

	@Inject
	protected EntityManager entityManager;
	
	@BeforeEach
	void resetDB() {
		this.entityManager.createNativeQuery(
				String.format("update hibernate_sequence set next_val=%d", this.getInitialID()))
		.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	protected List<UserEntity> getAllUsersFromDB() {
		return this.entityManager.createQuery("from UserEntity").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected List<PaymentEntity> getAllPaymentsFromDB() {
		return this.entityManager.createQuery("from PaymentEntity").getResultList();
	}
	
	protected abstract long getInitialID();

}