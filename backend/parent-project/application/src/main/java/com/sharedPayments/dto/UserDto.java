package com.sharedPayments.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.sharedPayments.model.User;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@Data
@NoArgsConstructor @AllArgsConstructor
public class UserDto {

	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;

	private BigDecimal debt;

	public UserDto(Long id, String name, Double debt) {
		this.id = id;
		this.name = name;
		this.setDebt(debt);
	}

	public UserDto(String name) {
		this(null, name, 0.0);
	}

	public BigDecimal getDebt() {
		if (this.debt == null)
			this.setDebt(0.0);
		return this.debt;
	}

	public void setDebt(Double debt) {
		if (debt == null)
			debt = 0.0;
		this.debt = new BigDecimal(debt).setScale(2, RoundingMode.HALF_EVEN);
	}

	public void setBDDebt(BigDecimal debt) {
		if (debt == null)
			debt = new BigDecimal(0);
		this.debt = debt.setScale(2, RoundingMode.HALF_EVEN);
	}

	public @Valid User toModel() {
		return new User(this.id, this.name, this.getDebt());
	}

}
