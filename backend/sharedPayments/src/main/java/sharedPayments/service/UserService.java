package sharedPayments.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import sharedPayments.model.User;
import sharedPayments.repository.UserRepository;

@Singleton
public class UserService {
	
	@Inject
	private UserRepository userRepository;

    public List<User> getUsers() {
    	List<User> result = new ArrayList<User>();
    	userRepository.findAll().forEach(result::add);
    	return result;
    }
    
    public User createUser(User newUser) {
    	return this.userRepository.save(newUser);
    }

	public Optional<User> getUser(Long userId) {
		return this.userRepository.findById(userId);
	}

	public void updateUserDebts(Long payerId, Double price) {
		var userList = this.getUsers();
		userRepository.findAll().forEach(user -> {
			if (user.getId() == payerId) 
				user.setDebt( user.getDebt() - (price - (price/userList.size())) );
			else
				user.setDebt( user.getDebt() + price/userList.size());
			userRepository.save(user);
		});
	}

}