package sharedPayments.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import sharedPayments.model.Payment;
import sharedPayments.model.User;

public class PaymentRepositoryIT extends RepositoryIT {
	
	@Test
	void givenNewPayment_WhenSaved_ThenPaymentsTableContainsPayment() throws SQLException {
		User payer = this.repoHandler.save(new User("Payer"));
		Payment payment = new Payment(payer, "NewPaymentDescription", 11.4);
		
		this.repoHandler.save(payment);
		var payments = QueryEnum.SELECT_ALL_PAYMENTS.execute(dbConfig);
		
		assertThat(payments.getString("description"), is(payment.getDescription()));
		assertThat(payments.getString("price"), is("11.40"));
	}

}
