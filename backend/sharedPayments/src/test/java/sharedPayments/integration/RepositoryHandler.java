package sharedPayments.integration;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.micronaut.context.annotation.Bean;
import sharedPayments.model.Payment;
import sharedPayments.model.User;
import sharedPayments.repository.PaymentRepository;
import sharedPayments.repository.UserRepository;

@Bean
public class RepositoryHandler {
	
	private UserRepository userRepository;
	private PaymentRepository paymentRepository;
	
	public RepositoryHandler(UserRepository userRepository, PaymentRepository paymentRepository) {
		this.userRepository = userRepository;
		this.paymentRepository = paymentRepository;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User save(User user) {
		return this.userRepository.save(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User update(User user) {
		return this.userRepository.update(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Payment save(Payment payment) {
		return this.paymentRepository.save(payment);
	}
	
	public List<User> findAllUsers() {
		return this.userRepository.findAll();
	}
	
	public List<Payment> findAllPayments() {
		return this.paymentRepository.findAll();
	}
	
	public Optional<User> findUserById(Long id) {
		return this.userRepository.findById(id);
	}

}
