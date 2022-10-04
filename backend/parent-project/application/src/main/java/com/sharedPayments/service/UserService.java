package com.sharedPayments.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sharedPayments.model.DebtCalculator;
import com.sharedPayments.model.User;
import com.sharedPayments.model.dto.MoneyMovementDto;
import com.sharedPayments.model.dto.UserDto;
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

	public void updateUserDebts(Long payerId, Double price) {
		int userCount = userRepository.count();
		if (userCount <= 0) return;
		
		DebtCalculator debtCalculator = new DebtCalculator();
		boolean addRoundingErrorCent;
		BigDecimal priceBD = BigDecimal.valueOf(price);
		BigDecimal userNumberBD = BigDecimal.valueOf(this.getUsers().size());
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

	public List<MoneyMovementDto> getMoneyMovementsToCompensateDebt() {
		return new DebtCalculator().calculateCompensationMovements(this.getUsers());
	}
}