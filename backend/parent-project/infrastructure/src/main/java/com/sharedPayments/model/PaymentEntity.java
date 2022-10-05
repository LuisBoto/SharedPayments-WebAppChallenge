package com.sharedPayments.model;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.micronaut.core.annotation.Introspected;

@Entity
@Table(name = "payment")
@Introspected
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity payer;

	private String description;

	@NotNull
	@Positive
	private BigDecimal price;

	@NotNull
	private Long paymentDate;

	public PaymentEntity() {
	}

	public PaymentEntity(@NotNull UserEntity payer, String description, @NotNull @Positive BigDecimal price, @NotNull Long paymentDate) {
		this.payer = payer;
		this.description = description;
		this.price = price;
		this.paymentDate = paymentDate;
	}
	
	public PaymentEntity(@NotNull UserEntity payer, String description, @NotNull @Positive double price, @NotNull Long paymentDate) {
		this(
			payer,
			description,
			BigDecimal.valueOf(price).setScale(2, RoundingMode.FLOOR),
			paymentDate);
	}
	
	public PaymentEntity(@NotNull UserEntity payer, String description, @NotNull @Positive double price) {
		this(
			payer,
			description,
			BigDecimal.valueOf(price).setScale(2, RoundingMode.FLOOR),
			System.currentTimeMillis());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.FLOOR);
	}

	public Long getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getPayer() {
		return payer;
	}

	public void setPayer(UserEntity payer) {
		this.payer = payer;
	}
	
	public static PaymentEntity fromModel(Payment payment) {
		PaymentEntity paymentE = new PaymentEntity(
				UserEntity.fromModel(payment.getPayer()),
				payment.getDescription(),
				payment.getPrice(),
				payment.getPaymentDate()
				);
		if (payment.getId() != null)
			paymentE.setId(payment.getId());
		System.out.println(paymentE.getId());
		return paymentE;
		
	}
	
	public Payment toModel() {
		return new Payment(
				this.id,
				this.payer.toModel(), 
				this.description, 
				this.price, 
				this.paymentDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, payer, paymentDate, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentEntity other = (PaymentEntity) obj;
		return Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(payer, other.payer) && Objects.equals(paymentDate, other.paymentDate)
				&& Objects.equals(price, other.price);
	}

}
