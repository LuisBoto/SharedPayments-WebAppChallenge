package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import sharedPayments.model.Payment;
import sharedPayments.model.User;
import sharedPayments.repository.PaymentRepository;
import sharedPayments.repository.UserRepository;

public class PaymentRepositoryIT extends GenericIT {
	
	@Inject
	private PaymentRepository paymentRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Override
	protected long getInitialID() {
		return 100L;
	}
	
	private void savePayments(Payment... payments) {
		for (Payment p : payments) 
			this.paymentRepository.save(p);
	}
	
	@Test
	void givenOneNewPayment_WhenSaved_ThenPaymentsTableContainsNewPayment() throws SQLException {
		User payer = this.userRepository.save(new User("Payer"));
		Payment payment = new Payment(payer, "NewPaymentDescription", 11.4);
		
		this.paymentRepository.save(payment);
		var payments = this.getAllPaymentsFromDB();
		
		assertEquals(7, payments.size()); 
		assertThat(payments.get(6).getDescription(), is(payment.getDescription()));
		assertThat(payments.get(6).getPrice(), is(11.4));
	}
	
	@Test
	void givenOneNewPayment_WhenSaved_ThenPaymentsTableContainsAll() throws SQLException {		
		User payer = this.userRepository.findById(2L).get();
		Payment payment = new Payment(payer, "newer payment", 77.89);
		payment = this.paymentRepository.save(payment);
		var payments = this.getAllPaymentsFromDB();
		
		assertEquals(7, payments.size());
		assertThat(payments.get(5).getDescription(), is("Tire"));
		assertThat(payments.get(6).getDescription(), is(payment.getDescription()));
		assertThat(payments.get(6).getId(), is(100L));
	}
	
	@Test
	void givenOneNewPayment_WhenSaved_ThenPaymentsTableContainsUserForeignKey() throws SQLException {
		User payer = this.userRepository.save(new User("Payer1")); // Id 100
		Payment payment = new Payment(payer, "NewPaymentWithUser", 25);
		
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
				new Payment(this.userRepository.findById(1L).get(), "First", 10.11),
				new Payment(this.userRepository.findById(2L).get(), "Second", 20.22),
				new Payment(this.userRepository.findById(3L).get(), "Third", 30.33));
		
		assertEquals(this.getAllPaymentsFromDB(), this.paymentRepository.findAll());
	}
	
	@Test
	void givenPayments_WhenFindById_ThenFoundPaymentHasPayerUserEntity() {	
		Payment payment = this.paymentRepository.findById(9L).get();
		assertThat(payment.getPayer(), is(notNullValue()));
		assertThat(payment.getPayer().getId(), is(4L));
		assertThat(payment.getPayer().getName(), is("Carla"));
	}
	
	@Test
	void givenOneNewPayment_WhenUpdateAmountAndPayer_ThenDatabaseHasUpdatedData() throws SQLException {
		User user1 = this.userRepository.save(new User("u1"));
		this.savePayments(new Payment(
				this.userRepository.findById(3L).get(), "oldDescription", 55.99));
		
		Payment payment = this.paymentRepository.findById(101L).get();
		payment.setDescription("newUpdatedDescription");
		payment.setPayer(user1);
		this.paymentRepository.update(payment);
		var payments = this.getAllPaymentsFromDB();
		
		assertEquals(7, payments.size());
		assertThat(payments.get(6).getDescription(), is(payment.getDescription()));
		assertThat(payments.get(6).getId(), is(101L));
	}

}
