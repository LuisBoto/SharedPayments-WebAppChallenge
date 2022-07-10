package sharedPayments.model.dto;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.micronaut.core.annotation.Introspected;
import sharedPayments.model.User;

@Introspected
public class UserDto {

	private Long id;
	
	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;
	@NotNull
	@Column(precision=2, scale=2)
	private double debt;

	public UserDto() { }
	
	public UserDto(Long id, String name, double debt) {
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
		return debt;
	}

	public void setDebt(double debt) {
		this.debt = debt;
	}

	public @Valid User toEntity() {
		return new User(this.name);
	}

}
