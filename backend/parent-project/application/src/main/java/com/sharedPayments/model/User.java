package com.sharedPayments.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.sharedPayments.model.dto.UserDto;

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
		return debt;
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
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
		User other = (User) obj;
		return Objects.equals(debt, other.debt) && Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
	
	public UserDto toDto() {
		return new UserDto(this.id, this.name, this.debt);
	}

}
