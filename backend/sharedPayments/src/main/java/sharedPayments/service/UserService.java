package sharedPayments.service;

import java.util.ArrayList;
import java.util.List;

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

}