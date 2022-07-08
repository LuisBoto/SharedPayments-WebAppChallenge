package sharedPayments.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.micronaut.core.annotation.Introspected;
import net.bytebuddy.implementation.bind.annotation.Default;
import sharedPayments.model.dto.PaymentDto;

@Entity
@Introspected
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private User payer;

	private String description;

	@NotNull
	@Positive
	private double price;

	@NotNull
	private Long paymentDate;

	public Payment() {
	}

	public Payment(@NotNull User payer, String description, @NotNull @Positive double price, @NotNull Long paymentDate) {
		super();
		this.payer = payer;
		this.description = description;
		this.price = price;
		this.paymentDate = paymentDate;
	}
	
	public Payment(@NotNull User payer, String description, @NotNull @Positive double price) {
		super();
		this.payer = payer;
		this.description = description;
		this.price = price;
		this.paymentDate = System.currentTimeMillis();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public User getPayer() {
		return payer;
	}

	public void setPayer(User payer) {
		this.payer = payer;
	}
	
	public PaymentDto toDto() {
		return new PaymentDto(
				this.payer.getId(), 
				this.paymentDate, 
				this.price, 
				this.description, 
				this.id);
	}

}
