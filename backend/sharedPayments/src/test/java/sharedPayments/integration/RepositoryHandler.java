package sharedPayments.integration;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.micronaut.context.annotation.Bean;
import sharedPayments.model.User;
import sharedPayments.repository.UserRepository;

@Bean
public class RepositoryHandler {
	
	private UserRepository userRepository;
	
	public RepositoryHandler(UserRepository repository) {
		this.userRepository = repository;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User save(User user) {
		return this.userRepository.save(user);
	}
	
	public List<User> findAll() {
		return this.userRepository.findAll();
	}

}
