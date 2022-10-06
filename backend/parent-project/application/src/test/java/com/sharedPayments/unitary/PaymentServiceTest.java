package com.sharedPayments.unitary;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.sharedPayments.dto.PaymentDto;
import com.sharedPayments.model.Payment;
import com.sharedPayments.model.User;
import com.sharedPayments.ports.PaymentRepository;
import com.sharedPayments.ports.UserRepository;
import com.sharedPayments.service.PaymentService;

public class PaymentServiceTest {

	private PaymentRepository paymentRepository = mock(PaymentRepository.class);
	private UserRepository userRepository = mock(UserRepository.class);
	private PaymentService paymentService = new PaymentService(this.paymentRepository, this.userRepository);
	
	@Test
	void givenTwoPayments_WhenGetAllPayments_ThenAllPaymentsAreReturnedAndSorted() {
		User payer = new User("Manola");
		List<Payment> payments = Arrays.asList(
				new Payment(payer, "Description", 500D, 100000L),
				new Payment(payer, "Description 2", 235D, 500000L),
				new Payment(payer, "Description 3", 800D, 3000L)
				);
		when(this.paymentRepository.findAll()).thenReturn(payments);
		List<PaymentDto> sortedPayments = Arrays.asList(
				payments.get(1).toDto(),
				payments.get(0).toDto(),
				payments.get(2).toDto()
				);

		assertThat(this.paymentService.getPayments(), is(sortedPayments));
	}
	
	@Test
	void givenNewValidPayment_WhenCreatePayment_ThenPaymentIsSaved() {
		PaymentDto paymentDto = new PaymentDto(5L, 10000L, 500.0, "Description", 15L);

		User user = new User();
		user.setId(5L);
		when(this.userRepository.findById(any())).thenReturn(user);
		when(this.paymentRepository.save(any())).then(call -> {
			paymentDto.setId(15L);
			Payment result = paymentDto.toModel(user);
			return result;
		});
		
		PaymentDto createdPayment = this.paymentService.createPayment(paymentDto);
		assertEquals(paymentDto, createdPayment);
		verify(this.paymentRepository).save(any());
	}
	
	@Test
	void givenNewPayment_WhenPayerUserDoesNotExist_ThenError() {
		when(this.userRepository.findById(any())).thenReturn(null);
		PaymentDto paymentDto = new PaymentDto(-7L, 10000L, 500.0, "Description", 15L);
		when(this.paymentRepository.save(any())).thenReturn(paymentDto.toModel(new User()));
		assertThrows(
				NoSuchElementException.class, 
				() -> this.paymentService.createPayment(paymentDto));
	}

}
