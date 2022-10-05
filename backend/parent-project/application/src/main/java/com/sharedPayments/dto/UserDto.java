package com.sharedPayments.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.sharedPayments.model.User;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class UserDto {

	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;

	private BigDecimal debt;

	public UserDto() {
	}

	public UserDto(Long id, String name, Double debt) {
		this.id = id;
		this.name = name;
		this.setDebt(debt);
	}

	public UserDto(String name) {
		this(null, name, 0.0);
	}

	public UserDto(Long id, String name, BigDecimal debt) {
		this(id, name, debt == null ? 0.0 : debt.doubleValue());

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getDebt() {
		if (this.debt == null) this.setDebt(0.0);
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

	@Override
	public int hashCode() {
		return Objects.hash(debt, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDto other = (UserDto) obj;
		return Objects.equals(debt, other.debt) && Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

}
