package com.sharedPayments.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.sharedPayments.dto.PaymentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
@Builder
public class Payment {

	private Long id;

	@NotNull
	private User payer;

	private String description;

	@NotNull
	@Positive
	private BigDecimal price;

	@NotNull
	private Long paymentDate;

	public PaymentDto toDto() {
		return new PaymentDto(this.payer.getId(), this.paymentDate, this.price, this.description, this.id);
	}
}
