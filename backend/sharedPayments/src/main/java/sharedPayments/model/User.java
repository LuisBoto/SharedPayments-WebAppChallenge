package sharedPayments.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.micronaut.core.annotation.Introspected;
import sharedPayments.model.dto.UserDto;

@Entity
@Table(name = "user")
@Introspected // Used to avoid reflective operations
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 75)
	private String name;

	@NotNull
	@Column(precision=2, scale=2)
	private double debt;

	public User() {
	}

	public User(@NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 20) String name) {
		super();
		this.name = name;
		this.debt = 0;
	}

	public double getDebt() {
		return debt;
	}

	public void setDebt(double debt) {
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

	public UserDto toDto() {
		return new UserDto(this.id, this.name, this.debt);
	}

}
