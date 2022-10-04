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
	
	public void updateUserDebts(Long payerId, Double price) {
		int userCount = userRepository.count();
		if (userCount <= 0) return;
		
		DebtCalculator debtCalculator = new DebtCalculator();
		boolean addRoundingErrorCent;
		BigDecimal priceBD = BigDecimal.valueOf(price);
		BigDecimal userNumberBD = BigDecimal.valueOf(userCount);
		BigDecimal roundingErrorCents = BigDecimal.valueOf((price*100) % userCount), resultDebt;
		
		for (User user : userRepository.findAll()) {
			if (user.getId() == payerId) 
				resultDebt = debtCalculator.calculatePayerDebt(
						user.getDebt(), priceBD, userNumberBD);
			else {
				addRoundingErrorCent = roundingErrorCents.doubleValue() > 0;
				resultDebt = debtCalculator.calculateOwerDebt(
						user.getDebt(), priceBD, userNumberBD, addRoundingErrorCent);
				if (addRoundingErrorCent) roundingErrorCents = roundingErrorCents.subtract(BigDecimal.valueOf(1));
			}
			
			user.setDebt(resultDebt);
			userRepository.save(user);
		}
	}
}