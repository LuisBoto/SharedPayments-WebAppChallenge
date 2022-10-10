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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor @Builder
@Introspected
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;

	@NotNull
	@Default
	private BigDecimal debt = new BigDecimal(0D).setScale(2, RoundingMode.HALF_EVEN);

	@OneToMany(mappedBy = "payer")
	private List<PaymentEntity> payments;

	public static UserEntity fromModel(User user) {
		UserEntity userE = UserEntity.builder().name(user.getName()).debt(user.getDebt()).build(); 
		if (user.getId() != null)
			userE.setId(user.getId());
		return userE;
	}

	public User toModel() {
		return new User(this.id, this.name, this.debt);
	}

}
