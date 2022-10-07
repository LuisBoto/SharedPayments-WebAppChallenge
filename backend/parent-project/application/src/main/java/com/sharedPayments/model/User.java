package com.sharedPayments.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.sharedPayments.dto.UserDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @EqualsAndHashCode
public class User {

	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;

	@NotNull
	private BigDecimal debt;

	public User(Long id, @NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 75) String name,
			@NotNull BigDecimal debt) {
		super();
		this.id = id;
		this.name = name;
		this.debt = debt;
	}

	public User(String name) {
		this.name = name;
		this.debt = BigDecimal.valueOf(0.0).setScale(2);
	}

	public User(String name, Double debt) {
		this.name = name;
		this.debt = BigDecimal.valueOf(debt).setScale(2);
	}
	
	public static List<User> updateUsersDebt(List<User> users, Long payerId, BigDecimal paymentPrice) {
		BigDecimal userCount = new BigDecimal(users.size());
		BigDecimal roundingErrorCents = paymentPrice.multiply(new BigDecimal(100)).remainder(userCount);
		for (User user : users) 
			roundingErrorCents = user.updateDebt(payerId, paymentPrice, userCount, roundingErrorCents);
		return users;
	}

	private BigDecimal updateDebt(Long payerId, BigDecimal paymentPrice, BigDecimal userCount, BigDecimal roundingErrorCents) {
		boolean addRoundingErrorCent = roundingErrorCents.doubleValue() > 0;
		if (this.id == payerId)
			this.setDebt(new DebtCalculator().calculatePayerDebt(
					this.debt, paymentPrice, userCount));
		else {
			this.setDebt(new DebtCalculator().calculateOwerDebt(
					this.debt, paymentPrice, userCount, addRoundingErrorCent));
			if (addRoundingErrorCent) 
				roundingErrorCents = roundingErrorCents.subtract(BigDecimal.valueOf(1)).setScale(2, RoundingMode.HALF_EVEN);
		}
			
		return roundingErrorCents;
	}

	private void setDebt(BigDecimal debt) {
		this.debt = debt.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public UserDto toDto() {
		return new UserDto(this.id, this.name, this.debt);
	}

}
