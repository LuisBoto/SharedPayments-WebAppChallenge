package com.sharedPayments.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sharedPayments.dto.MoneyMovementDto;
import com.sharedPayments.dto.UserDto;
import com.sharedPayments.model.DebtCalculator;
import com.sharedPayments.model.User;
import com.sharedPayments.ports.UserRepository;

import jakarta.inject.Singleton;

@Singleton
public class UserService {
	
	private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserDto> getUsers() {
    	List<UserDto> result = new ArrayList<UserDto>();
    	userRepository.findAll().forEach(user -> result.add(user.toDto()));
    	return result;
    }
    
    public UserDto createUser(UserDto newUser) {
    	return this.userRepository.save(newUser.toModel()).toDto();
    }

	public List<MoneyMovementDto> getMoneyMovementsToCompensateDebt() {
		return new DebtCalculator().calculateCompensationMovements(this.getUsers());
	}
	
	public void updateUserDebts(Long payerId, BigDecimal price) {
		if (this.userRepository.count() <= 0) 
			return;
		
		List<User> updatedUsers = User.updateUsersDebt(this.userRepository.findAll(), payerId, price);
		updatedUsers.forEach(u -> userRepository.update(u));
	}
}