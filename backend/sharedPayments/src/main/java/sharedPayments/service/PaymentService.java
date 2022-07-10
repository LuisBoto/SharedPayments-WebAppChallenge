package sharedPayments.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import sharedPayments.model.User;
import sharedPayments.model.dto.PaymentDto;
import sharedPayments.repository.PaymentRepository;

@Singleton
public class PaymentService {

	@Inject
	private PaymentRepository paymentRepository;
	
	private Comparator<PaymentDto> comparePaymentsByDate = ((PaymentDto o1, PaymentDto o2)-> 
			o1.getPaymentDate() <= o2.getPaymentDate() ?1:-1);

	public List<PaymentDto> getPayments() {
		List<PaymentDto> result = new ArrayList<PaymentDto>();
		paymentRepository.findAll().forEach(payment -> result.add(payment.toDto()));
		result.sort(this.comparePaymentsByDate);
		return result;
	}

	public PaymentDto createPayment(PaymentDto newPayment, Optional<User> payerUser) {
		if (payerUser.isEmpty())
			throw new NoSuchElementException("Payer User not found");

		return this.paymentRepository.save(newPayment.toEntity(payerUser.get())).toDto();
	}

}