package com.sharedPayments.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.sharedPayments.model.User;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserDto {

	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;

	@Default
	private BigDecimal debt = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);

	public void setBDDebt(BigDecimal debt) {
		if (debt == null)
			debt = new BigDecimal(0);
		this.debt = debt.setScale(2, RoundingMode.HALF_EVEN);
	}

	public @Valid User toModel() {
		return new User(this.id, this.name, this.getDebt());
	}

}
