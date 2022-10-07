package com.sharedPayments.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.sharedPayments.dto.PaymentDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @EqualsAndHashCode
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

	public Payment(@NotNull User payer, String description, @NotNull @Positive double price) {
		this.payer = payer;
		this.description = description;
		this.price = BigDecimal.valueOf(price).setScale(2);
	}

	public Payment(Long id, User payer, String description, BigDecimal price, @NotNull Long paymentDate) {
		this(payer, description, price.doubleValue(), paymentDate);
		this.id = id;
	}

	public Payment(User payer, String description, double price, @NotNull Long paymentDate) {
		this(payer, description, price);
		this.paymentDate = paymentDate;
	}

	public PaymentDto toDto() {
		return new PaymentDto(this.payer.getId(), this.paymentDate, this.price, this.description, this.id);
	}
}
