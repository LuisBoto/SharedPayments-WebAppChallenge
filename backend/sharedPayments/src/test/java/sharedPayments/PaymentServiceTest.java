package sharedPayments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import sharedPayments.model.Payment;
import sharedPayments.model.User;
import sharedPayments.model.dto.PaymentDto;
import sharedPayments.repository.PaymentRepository;
import sharedPayments.service.PaymentService;

public class PaymentServiceTest {

	private PaymentRepository paymentRepository = mock(PaymentRepository.class);
	private PaymentService paymentService = new PaymentService(this.paymentRepository);
	
	@Test
	void givenTwoPayments_WhenGetAllPayments_ThenSizeIsTwo() {
		when(this.paymentRepository.findAll()).thenReturn( Arrays.asList(
				new Payment(new User(), "Description", 500),
				new Payment(new User(), "Description 2", 235)
				));

		assertTrue(this.paymentService.getPayments().size() == 2);
	}
	
	@Test
	void givenSeveralPayments_WhenGetAllPayments_ThenPaymentsAreSortedByDate() {
		when(this.paymentRepository.findAll()).thenReturn( Arrays.asList(
				new Payment(new User(), "Description", 500, 100000L),
				new Payment(new User(), "Description 2", 235, 500000L),
				new Payment(new User(), "Description 3", 800, 3000L)
				));
		
		List<PaymentDto> payments = this.paymentService.getPayments();
		assertEquals(3000L, payments.get(2).getPaymentDate());
		assertEquals(100000L, payments.get(1).getPaymentDate());
		assertEquals(500000L, payments.get(0).getPaymentDate());
	}
	
	@Test
	void givenNewValidPayment_WhenCreatePayment_ThenPaymentIsSaved() {
		PaymentDto paymentDto = new PaymentDto(5L, 10000L, 500.0, "Description", 15L);

		User user = new User();
		user.setId(5L);
		when(this.paymentRepository.save(any())).then(call -> {
			Payment result = paymentDto.toEntity(user);
			result.setId(15L);
			return result;
		});
		
		PaymentDto createdPayment = this.paymentService.createPayment(paymentDto, Optional.of(user));
		assertTrue(paymentDto.equals(createdPayment));
	}
	
	@Test
	void givenNewPayment_WhenPayerUserDoesNotExist_ThenError() {
		PaymentDto paymentDto = new PaymentDto(1L, 10000L, 500.0, "Description", 15L);
		when(this.paymentRepository.save(any())).thenReturn(paymentDto.toEntity(new User()));
		assertThrows(
				NoSuchElementException.class, 
				() -> this.paymentService.createPayment(paymentDto, Optional.empty()));
	}

}
