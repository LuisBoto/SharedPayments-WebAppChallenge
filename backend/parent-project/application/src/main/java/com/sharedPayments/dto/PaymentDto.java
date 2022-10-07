package com.sharedPayments.dto;

import java.math.BigDecimal;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.sharedPayments.model.Payment;
import com.sharedPayments.model.User;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;

@Introspected
@Data @AllArgsConstructor
public class PaymentDto {

	@NotNull
	private Long payerId;
	
	private Long paymentDate;
	
	@NotNull
	@Positive
	private BigDecimal price;
	
	private String description;
	
	private Long id;

	public PaymentDto() {
	}
	
	public PaymentDto(Long payerId, Long paymentDate, double price, String description, Long id) {
		this(payerId, 
				paymentDate, 
				BigDecimal.valueOf(price).setScale(2), 
				description, 
				id);
	}
	
	public PaymentDto(Long payerId, BigDecimal price, String description, Long id) {
		this(payerId, 
				System.currentTimeMillis(), 
				price, 
				description, 
				id);
	}
	
	public PaymentDto(Long payerId, double price, String description, Long id) {
		this(payerId, 
				BigDecimal.valueOf(price).setScale(2), 
				description, 
				id);
	}

	public Long getPaymentDate() {
		if (paymentDate==null) return System.currentTimeMillis();
		return paymentDate;
	}

	public void setPrice(Double price) {
		this.price = BigDecimal.valueOf(price);
	}

	public @Valid Payment toModel(User payer) {
		return new Payment(
				this.id, 
				payer, 
				this.description, 
				this.price, 
				this.getPaymentDate());
	}

}
