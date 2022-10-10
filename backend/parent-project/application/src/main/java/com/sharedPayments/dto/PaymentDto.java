package com.sharedPayments.dto;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.sharedPayments.model.Payment;
import com.sharedPayments.model.User;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentDto {

	@NotNull
	private Long payerId;
	
	@Default
	private Long paymentDate = System.currentTimeMillis();
	
	@NotNull
	@Positive
	private BigDecimal price;
	
	private String description;
	
	private Long id;

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
