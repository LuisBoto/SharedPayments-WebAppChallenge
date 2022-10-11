package com.sharedPayments.unitary;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
		User payer = User.builder().name("Manola").build(); 
		List<Payment> payments = Arrays.asList(
				Payment.builder().payer(payer).description("Description").price(new BigDecimal(500)).paymentDate(50000L).build(),
				Payment.builder().payer(payer).description("Description 2").price(new BigDecimal(500)).paymentDate(100000L).build(),
				Payment.builder().payer(payer).description("Description 3").price(new BigDecimal(500)).paymentDate(3000L).build());
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
		PaymentDto paymentDto = PaymentDto.builder()
				.payerId(5L).description("Description").paymentDate(10000L).price(new BigDecimal(500.00).setScale(2)).id(15L).build();

		User user = new User(5L, "Juan", new BigDecimal(0));
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
		PaymentDto paymentDto = PaymentDto.builder()
				.payerId(-7L).description("Description").paymentDate(10000L).price(new BigDecimal(500)).id(15L).build();
		when(this.paymentRepository.save(any())).thenReturn(paymentDto.toModel(new User()));
		assertThrows(
				NoSuchElementException.class, 
				() -> this.paymentService.createPayment(paymentDto));
	}

}
