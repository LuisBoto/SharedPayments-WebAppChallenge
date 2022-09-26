package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import sharedPayments.model.Payment;
import sharedPayments.model.User;

public class PaymentRepositoryIT extends RepositoryIT {
	
	private void saveDatabasePayments(Payment... payments) {
		for (Payment payment : payments) {
			payment.setPayer(this.repoHandler.save(new User("User " + RepositoryIT.currentId)));
			payment.setId(RepositoryIT.currentId);
			QueryEnum.INSERT_INTO_PAYMENTS.executeFormatted(dbConfig, 
					payment.getId(), 
					payment.getDescription(), 
					payment.getPaymentDate(), 
					payment.getPrice(), 
					payment.getPayer().getId());
			//this.repoHandler.save(payment);
		}
	}
	
	@Test
	void givenNoPayments_WhenSaveNewPayment_ThenPaymentsTableContainsPayment() throws SQLException {
		User payer = this.repoHandler.save(new User("Payer"));
		Payment payment = new Payment(payer, "NewPaymentDescription", 11.4);
		
		this.repoHandler.save(payment);
		var payments = QueryEnum.SELECT_ALL_PAYMENTS.execute(dbConfig);
		
		assertThat(payments.getString("description"), is(payment.getDescription()));
		assertThat(payments.getString("price"), is("11.40"));
	}
	
	@Test
	void givenOnePayment_WhenSaveNewPayment_ThenPaymentsTableContainsBoth() throws SQLException {
		Payment oldPayment = new Payment(null, "old payment", 33.33);
		this.saveDatabasePayments(oldPayment);
		
		User payer = this.repoHandler.save(new User("Payer"));
		Payment payment = new Payment(payer, "newer payment", 77.89);
		payment = this.repoHandler.save(payment);
		var payments = QueryEnum.SELECT_ALL_PAYMENTS.execute(dbConfig);
		
		assertThat(payments.getString("description"), is(oldPayment.getDescription()));
		assertThat(payments.next(), is(true));
		assertThat(payments.getString("description"), is(payment.getDescription()));
		assertThat(payments.getString("id"), is("4"));
	}
	
	@Test
	void givenNoPayments_WhenNewPaymentIsSaved_ThenPaymentsTableContainsUserForeignKey() throws SQLException {
		User payer = this.repoHandler.save(new User("Payer")); // Id 1
		Payment payment = new Payment(payer, "NewPaymentWithUser", 25);
		
		this.repoHandler.save(payment);
		var payments = QueryEnum.SELECT_ALL_PAYMENTS.execute(dbConfig);
		
		assertThat(payments.getString("payer_id"), is("1"));
		assertThat(payments.getString("id"), is("2"));
	}
	
	@Test
	void givenNoPayments_WhenFindAll_ThenListIsEmpty() {
		assertThat(this.repoHandler.findAllPayments().size(), is(0));
	}
	
	@Test
	void givenSeveralPayments_WhenFindAll_ThenListContainsAllUsers() {
		Payment[] dbPayments = {
				new Payment(null, "First", 10.11),
				new Payment(null, "Second", 20.22),
				new Payment(null, "Third", 30.33)};
		this.saveDatabasePayments(dbPayments);
		List<Payment> payments = this.repoHandler.findAllPayments();
		
		assertEquals(Arrays.asList(dbPayments), payments);
	}
	
	@Test
	void givenFourPayments_WhenFindById_ThenFoundPaymentHasPayerUserEntity() {
		this.saveDatabasePayments(
				new Payment(null, "Payment1", 100), // Id 2
				new Payment(null, "Payment2", 200), // Id 4
				new Payment(null, "Payment3", 300), // Id 6
				new Payment(null, "Payment4", 400)); // Id 8
		
		Payment payment = this.repoHandler.findPaymentById(4L).get();
		assertThat(payment.getPayer(), is(notNullValue()));
		assertThat(payment.getPayer().getId().toString(), is("3"));
		assertThat(payment.getPayer().getName(), is("User 3"));
	}
	
	@Test
	void givenOnePayment_WhenUpdateAmountAndPayer_ThenDatabaseHasUpdatedData() throws SQLException {
		User user1 = this.repoHandler.save(new User("u1"));
		this.saveDatabasePayments(new Payment(null, "oldDescription", 55.99));
		
		Payment payment = this.repoHandler.findPaymentById(3L).get();
		payment.setDescription("newUpdatedDescription");
		payment.setPayer(user1);
		this.repoHandler.update(payment);
		var payments = QueryEnum.SELECT_ALL_PAYMENTS.execute(dbConfig);
		
		assertThat(payments.getString("description"), is(payment.getDescription()));
		assertThat(payments.getString("payer_id"), is("1"));
	}

}
