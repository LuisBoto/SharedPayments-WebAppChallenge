package com.sharedPayments.integration;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;

import com.sharedPayments.model.Payment;
import com.sharedPayments.model.User;

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
	protected List<User> getAllUsersFromDB() {
		return this.entityManager.createQuery("from User").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected List<Payment> getAllPaymentsFromDB() {
		return this.entityManager.createQuery("from Payment").getResultList();
	}
	
	protected abstract long getInitialID();

}
