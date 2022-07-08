package sharedPayments.model.dto;

import javax.validation.Valid;

import sharedPayments.model.User;

public class UserDto {

	private Long id;
	private String name;
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
