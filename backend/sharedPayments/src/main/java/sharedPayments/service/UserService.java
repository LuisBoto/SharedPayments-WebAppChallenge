package sharedPayments.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Singleton;
import sharedPayments.model.User;
import sharedPayments.model.dto.MoneyMovementDto;
import sharedPayments.model.dto.UserDto;
import sharedPayments.model.utils.DebtCalculator;
import sharedPayments.repository.UserRepository;

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
    	return this.userRepository.save(newUser.toEntity()).toDto();
    }

	public Optional<User> getUser(Long userId) {
		return this.userRepository.findById(userId);
	}

	public void updateUserDebts(Long payerId, Double price) {
		int userCount = (int) userRepository.count();
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