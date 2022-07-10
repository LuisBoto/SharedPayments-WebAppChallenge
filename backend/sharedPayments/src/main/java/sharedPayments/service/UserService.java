package sharedPayments.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		BigDecimal priceBD = BigDecimal.valueOf(price), userNoBD = BigDecimal.valueOf(userList.size());
		BigDecimal extraCents = BigDecimal.valueOf((price*100) % userRepository.count());
		BigDecimal roundingErrorCents = extraCents, resultDebt;
		//System.out.println(priceBD.subtract(priceBD.divide(userNoBD, 2, RoundingMode.FLOOR)));
		
		for (User user : userRepository.findAll()) {
			if (user.getId() == payerId) 
				resultDebt = user.getBDDebt().subtract(
						priceBD.subtract(priceBD.divide(userNoBD, 2, RoundingMode.FLOOR)));
						//.subtract(extraCents.divide(BigDecimal.valueOf(100.0)));
			else {
				resultDebt = user.getBDDebt().add(
						priceBD.divide(userNoBD, 2, RoundingMode.FLOOR));
				if (roundingErrorCents.doubleValue() > 0) {
					roundingErrorCents = roundingErrorCents.subtract(BigDecimal.valueOf(1));
					resultDebt = resultDebt.add(BigDecimal.valueOf(0.01));
				}
			}

			user.setDebt(resultDebt.doubleValue());
			userRepository.save(user);
		}
	}
}