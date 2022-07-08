package sharedPayments.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import sharedPayments.model.User;
import sharedPayments.model.dto.UserDto;
import sharedPayments.repository.UserRepository;

@Singleton
public class UserService {
	
	@Inject
	private UserRepository userRepository;

    public List<UserDto> getUsers() {
    	List<UserDto> result = new ArrayList<UserDto>();
    	userRepository.findAll().forEach(user -> result.add(user.toDto()));
    	return result;
    }
    
    public UserDto createUser(UserDto newUser) {
    	return this.userRepository.save(newUser.toEntity()).toDto();
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