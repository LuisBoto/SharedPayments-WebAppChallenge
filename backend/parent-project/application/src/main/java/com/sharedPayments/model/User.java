package com.sharedPayments.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.sharedPayments.model.dto.UserDto;

import io.micronaut.core.annotation.Introspected;

@Entity
@Table(name = "user")
@Introspected(packages="com.sharedPayments.model", includedAnnotations=Entity.class)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;

	@NotNull
	private BigDecimal debt;

	public User() {
	}
	
	public User(@NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 20) String name, Double debt) {
		super();
		this.name = name;
		this.debt = BigDecimal.valueOf(debt).setScale(2, RoundingMode.FLOOR);
	}

	public User(@NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 20) String name) {
		this(name, 0D);
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}
	
	public BigDecimal getDebt() {
		return this.debt;
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

	public UserDto toDto() {
		return new UserDto(this.id, this.name, this.debt);
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

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", debt=" + debt + "]";
	}
	
}
