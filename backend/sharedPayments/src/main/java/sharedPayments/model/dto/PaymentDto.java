package sharedPayments.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.micronaut.core.annotation.Introspected;
import sharedPayments.model.Payment;
import sharedPayments.model.User;

@Introspected
public class PaymentDto {

	@NotNull
	private Long payerId;
	
	private Long paymentDate;
	
	@NotNull
	private Double price;
	
	private String description;
	
	private Long id;

	public PaymentDto() {
	}

	public PaymentDto(Long payerId, Long paymentDate, Double price, String description, Long id) {
		super();
		this.payerId = payerId;
		this.paymentDate = paymentDate;
		this.price = price;
		this.description = description;
		this.id = id;
	}
	
	public PaymentDto(Long payerId, Double price, String description, Long id) {
		super();
		this.payerId = payerId;
		this.paymentDate = System.currentTimeMillis();
		this.price = price;
		this.description = description;
		this.id = id;
	}

	public Long getPayerId() {
		return payerId;
	}

	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}

	public Long getPaymentDate() {
		if (paymentDate==null) return System.currentTimeMillis();
		return paymentDate;
	}

	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public @Valid Payment toEntity(User payer) {
		return new Payment(
				payer, 
				this.description, 
				this.price, 
				this.getPaymentDate());
	}

}
