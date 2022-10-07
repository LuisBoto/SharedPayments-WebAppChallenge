package com.sharedPayments.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor
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

	@OneToMany(mappedBy = "payer")
	private List<PaymentEntity> payments;

	public UserEntity(@NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 20) String name, BigDecimal debt) {
		this.name = name;
		this.debt = debt.setScale(2, RoundingMode.FLOOR);
	}

	public UserEntity(@NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 20) String name) {
		this(name, new BigDecimal(0D));
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

}
