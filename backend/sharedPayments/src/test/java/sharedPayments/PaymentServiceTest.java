package sharedPayments;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sharedPayments.model.Payment;
import sharedPayments.model.User;
import sharedPayments.repository.PaymentRepository;
import sharedPayments.service.PaymentService;

public class PaymentServiceTest {
	
	private PaymentService paymentService;
	
	private PaymentRepository paymentRepository = mock(PaymentRepository.class);
	
	@BeforeEach
	void before() {
		paymentService = new PaymentService(this.paymentRepository);
	}
	
	@Test
	void givenNoPayments_WhenGetAllPayments_ThenSizeIsZero() {
		when(this.paymentRepository.findAll()).thenReturn( new ArrayList<Payment>() );
		
		assertTrue(this.paymentService.getPayments().size() == 0);
	}
	
	@Test
	void givenTwoPayments_WhenGetAllPayments_ThenSizeIsTwo() {
		when(this.paymentRepository.findAll()).thenReturn( Arrays.asList(
				new Payment(new User(), "Description", 500),
				new Payment(new User(), "Description 2", 235)
				));

		assertTrue(this.paymentService.getPayments().size() == 2);
	}

}
