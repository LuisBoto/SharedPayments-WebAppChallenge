package com.sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.sharedPayments.model.PaymentEntity;
import com.sharedPayments.model.UserEntity;
import com.sharedPayments.repository.PaymentInfraJpaRepository;
import com.sharedPayments.repository.UserInfraJpaRepository;

import jakarta.inject.Inject;

public class PaymentRepositoryIT extends GenericIT {

	@Inject
	private PaymentInfraJpaRepository paymentRepository;
	
	@Inject
	private UserInfraJpaRepository userRepository;
	
	@Override
	protected long getInitialID() {
		return 100L;
	}
	
	private void savePayments(PaymentEntity... payments) {
		for (PaymentEntity p : payments) 
			this.paymentRepository.save(p);
	}
	
	@Test
	void givenOneNewPayment_WhenSaved_ThenPaymentsTableContainsNewPayment() throws SQLException {
		UserEntity payer = this.userRepository.save(UserEntity.builder().name("Payer").build());
		PaymentEntity payment = PaymentEntity.builder().payer(payer).description("NewPaymentDescription").build(); 
		payment.setPrice(11.4);
		
		this.paymentRepository.save(payment);
		var payments = this.getAllPaymentsFromDB();
		
		assertEquals(7, payments.size()); 
		assertThat(payments.get(6).getDescription(), is(payment.getDescription()));
		assertThat(payments.get(6).getPrice().toString(), is("11.40"));
	}
	
	@Test
	void givenOneNewPayment_WhenSaved_ThenPaymentsTableContainsAll() throws SQLException {		
		UserEntity payer = this.userRepository.findById(2L).get();
		PaymentEntity payment = PaymentEntity.builder().payer(payer).description("newer payment").price(new BigDecimal(77.89)).build(); 

		payment = this.paymentRepository.save(payment);
		var payments = this.getAllPaymentsFromDB();
		
		assertEquals(7, payments.size());
		assertThat(payments.get(5).getDescription(), is("Tire"));
		assertThat(payments.get(6).getDescription(), is(payment.getDescription()));
		assertThat(payments.get(6).getId(), is(100L));
	}
	
	@Test
	void givenOneNewPayment_WhenSaved_ThenPaymentsTableContainsUserForeignKey() throws SQLException {
		UserEntity payer = this.userRepository.save(UserEntity.builder().name("Payer1").build()); // Id 100
		PaymentEntity payment = PaymentEntity.builder().payer(payer).description("NewPaymentWithUser").price(new BigDecimal(25)).build(); 
		
		this.paymentRepository.save(payment);
		var payments = this.getAllPaymentsFromDB();
		
		assertEquals(7, payments.size());
		assertThat(payments.get(6).getPayer().getId(), is(payer.getId()));
		assertThat(payments.get(6).getId(), is(101L));
	}
	
	@Test
	void givenPayments_WhenFindAll_ThenListContainsAllPayments() throws SQLException {
		assertEquals(this.getAllPaymentsFromDB(), this.paymentRepository.findAll());
	}
	
	@Test
	void givenTwoNewPayments_WhenFindAll_ThenListContainsAllPayments() throws SQLException {
		this.savePayments(
				PaymentEntity.builder().payer(this.userRepository.findById(1L).get()).description("First").price(new BigDecimal(10.11)).build(),
				PaymentEntity.builder().payer(this.userRepository.findById(2L).get()).description("Second").price(new BigDecimal(20.22)).build(),
				PaymentEntity.builder().payer(this.userRepository.findById(3L).get()).description("Third").price(new BigDecimal(30.33)).build()); 
	
		
		assertEquals(this.getAllPaymentsFromDB(), this.paymentRepository.findAll());
	}
	
	@Test
	void givenPayments_WhenFindById_ThenFoundPaymentHasPayerUserEntity() {	
		PaymentEntity payment = this.paymentRepository.findById(9L).get();
		assertThat(payment.getPayer(), is(notNullValue()));
		assertThat(payment.getPayer().getId(), is(4L));
		assertThat(payment.getPayer().getName(), is("Carla"));
	}
	
	@Test
	void givenOneNewPayment_WhenUpdateAmountAndPayer_ThenDatabaseHasUpdatedData() throws SQLException {
		UserEntity user1 = this.userRepository.save(UserEntity.builder().name("u1").build());
		this.savePayments(
				PaymentEntity.builder().payer(this.userRepository.findById(3L).get()).description("oldDescription").price(new BigDecimal(55.99)).build());
		
		PaymentEntity payment = this.paymentRepository.findById(101L).get();
		payment.setDescription("newUpdatedDescription");
		payment.setPayer(user1);
		this.paymentRepository.update(payment);
		var payments = this.getAllPaymentsFromDB();
		
		assertEquals(7, payments.size());
		assertThat(payments.get(6).getDescription(), is(payment.getDescription()));
		assertThat(payments.get(6).getId(), is(101L));
	}

}
