package sharedPayments.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.micronaut.core.annotation.Introspected;
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
	private BigDecimal price;

	@NotNull
	private Long paymentDate;

	public Payment() {
	}

	public Payment(@NotNull User payer, String description, @NotNull @Positive double price, @NotNull Long paymentDate) {
		super();
		this.payer = payer;
		this.description = description;
		this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.FLOOR);
		this.paymentDate = paymentDate;
	}
	
	public Payment(@NotNull User payer, String description, @NotNull @Positive double price) {
		super();
		this.payer = payer;
		this.description = description;
		this.price = BigDecimal.valueOf(price).setScale(2);
		this.paymentDate = System.currentTimeMillis();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price.doubleValue();
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
				this.price.doubleValue(), 
				this.description, 
				this.id);
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

}
