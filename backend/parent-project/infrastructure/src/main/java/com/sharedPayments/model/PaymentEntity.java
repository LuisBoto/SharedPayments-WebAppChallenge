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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor
@Builder
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
	@Default
	private Long paymentDate = System.currentTimeMillis();

	public void setPrice(double price) {
		this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price.setScale(2, RoundingMode.HALF_EVEN);
	}

	public static PaymentEntity fromModel(Payment payment) {
		PaymentEntity paymentE = PaymentEntity.builder()
				.payer(UserEntity.fromModel(payment.getPayer()))
				.description(payment.getDescription())
				.price(payment.getPrice())
				.paymentDate(payment.getPaymentDate())
				.build();
		if (payment.getId() != null)
			paymentE.setId(payment.getId());
		return paymentE;
	}

	public Payment toModel() {
		return new Payment(this.id, this.payer.toModel(), this.description, this.price.setScale(2, RoundingMode.HALF_EVEN), this.paymentDate);
	}

}
