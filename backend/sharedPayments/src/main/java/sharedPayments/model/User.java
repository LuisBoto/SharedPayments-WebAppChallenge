package sharedPayments.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.micronaut.core.annotation.Introspected;


@Entity
@Introspected // Used to avoid reflective operations
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Cannot be empty")
	@Size(min = 1, max = 20)
	private String name;

	public User() { }
	
	public User(@NotEmpty(message = "Cannot be empty") @Size(min = 1, max = 20) String name) {
		super();
		this.name = name;
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

}
