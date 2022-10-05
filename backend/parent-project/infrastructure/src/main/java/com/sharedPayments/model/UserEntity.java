package com.sharedPayments.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.micronaut.core.annotation.Introspected;

@Entity
@Table(name = "user")
@Introspected
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;

	@NotNull
	private BigDecimal debt;

	@OneToMany(mappedBy = "payer")//, cascade = CascadeType.ALL)
	private List<PaymentEntity> payments;

	public UserEntity() {
	}

	public UserEntity(@NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 20) String name, BigDecimal debt) {
		this.name = name;
		this.debt = debt.setScale(2, RoundingMode.FLOOR);
	}

	public UserEntity(@NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 20) String name) {
		this(name, new BigDecimal(0D));
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

	public List<PaymentEntity> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentEntity> payments) {
		this.payments = payments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static UserEntity fromModel(User user) {
		UserEntity userE = new UserEntity(user.getName(), user.getDebt());
		if (user.getId() != null)
			userE.setId(user.getId());
		return userE;
	}

	public User toModel() {
		return new User(this.id, this.name, this.debt);
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
		UserEntity other = (UserEntity) obj;
		return Objects.equals(debt, other.debt) && Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", debt=" + debt + "]";
	}

}