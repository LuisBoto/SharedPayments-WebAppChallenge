package com.sharedPayments.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor
@Introspected
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private UserEntity payer;

	private String description;

	@NotNull
	@Positive
	private BigDecimal price;

	@NotNull
	private Long paymentDate;

	public PaymentEntity(@NotNull UserEntity payer, String description, @NotNull @Positive BigDecimal price,
			@NotNull Long paymentDate) {
		this.payer = payer;
		this.description = description;
		this.price = price;
		this.paymentDate = paymentDate;
	}

	public PaymentEntity(@NotNull UserEntity payer, String description, @NotNull @Positive double price,
			@NotNull Long paymentDate) {
		this(payer, description, BigDecimal.valueOf(price).setScale(2, RoundingMode.FLOOR), paymentDate);
	}

	public PaymentEntity(@NotNull UserEntity payer, String description, @NotNull @Positive double price) {
		this(payer, description, BigDecimal.valueOf(price).setScale(2, RoundingMode.FLOOR), System.currentTimeMillis());
	}

	public void setPrice(double price) {
		this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.FLOOR);
	}

	public static PaymentEntity fromModel(Payment payment) {
		PaymentEntity paymentE = new PaymentEntity(
				UserEntity.fromModel(payment.getPayer()), 
				payment.getDescription(),
				payment.getPrice(), 
				payment.getPaymentDate());
		if (payment.getId() != null)
			paymentE.setId(payment.getId());
		return paymentE;
	}

	public Payment toModel() {
		return new Payment(this.id, this.payer.toModel(), this.description, this.price, this.paymentDate);
	}

}
