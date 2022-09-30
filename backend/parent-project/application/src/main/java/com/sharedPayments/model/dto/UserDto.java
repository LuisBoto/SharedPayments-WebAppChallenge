package com.sharedPayments.model.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.sharedPayments.model.User;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class UserDto {

	private Long id;
	
	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;
	@NotNull
	private BigDecimal debt;

	public UserDto() { }
	
	public UserDto(Long id, String name, double debt) {
		super();
		this.id = id;
		this.name = name;
		this.debt = BigDecimal.valueOf(debt).setScale(2, RoundingMode.FLOOR);
	}
	
	public UserDto(Long id, String name, BigDecimal debt) {
		super();
		this.id = id;
		this.name = name;
		this.debt = debt;
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

	public double getDebt() {
		return debt!=null ? debt.doubleValue() : 0.0;
	}
	
	public BigDecimal getBDDebt() {
		return this.debt;
	}

	public void setDebt(double debt) {
		this.debt = BigDecimal.valueOf(debt).setScale(2, RoundingMode.FLOOR);
	}

	public @Valid User toEntity() {
		User result = new User(this.name);
		result.setDebt(this.debt);
		result.setId(this.id);
		return result;
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
