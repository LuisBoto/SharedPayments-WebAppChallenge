package com.sharedPayments.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.sharedPayments.dto.PaymentDto;

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

	public Payment(Long id, @NotNull User payer, String description, @NotNull @Positive BigDecimal price,
			@NotNull Long paymentDate) {
		super();
		this.id = id;
		this.payer = payer;
		this.description = description;
		this.price = price;
		this.paymentDate = paymentDate;
	}

	public Payment(User payer, String description, double price) {
		this.payer = payer;
		this.description = description;
		this.price = BigDecimal.valueOf(price).setScale(2);
	}

	public Payment(User payer, String description, double price, long paymentDate) {
		this.payer = payer;
		this.description = description;
		this.price = BigDecimal.valueOf(price).setScale(2);
		this.paymentDate = paymentDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getPayer() {
		return payer;
	}

	public void setPayer(User payer) {
		this.payer = payer;
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

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
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
		Payment other = (Payment) obj;
		return Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(payer, other.payer) && Objects.equals(paymentDate, other.paymentDate)
				&& Objects.equals(price, other.price);
	}

	public PaymentDto toDto() {
		return new PaymentDto(this.payer.getId(), this.paymentDate, this.price, this.description, this.id);
	}
}
