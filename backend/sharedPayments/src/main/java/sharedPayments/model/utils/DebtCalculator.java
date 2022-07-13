package sharedPayments.model.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import sharedPayments.model.dto.MoneyMovementDto;
import sharedPayments.model.dto.UserDto;

public class DebtCalculator {

	private List<UserDto> usersOwedMoney;
	private List<UserDto> usersOwingMoney;
	private UserDto userOwedMoney;
	private UserDto userOwingMoney;
	private List<MoneyMovementDto> movements;
	

	private Comparator<UserDto> compareUsersByDebt = ((UserDto o1, UserDto o2) -> {
		return o1.getDebt() <= o2.getDebt() ? 1 : -1;
	});

	public DebtCalculator() {
		this.initialize();
	}
	
	private void initialize() {
		this.usersOwedMoney = new ArrayList<UserDto>();
		this.usersOwingMoney = new ArrayList<UserDto>();
		this.movements = new ArrayList<MoneyMovementDto>();
	}

	public List<MoneyMovementDto> calculateCompensationMovements(List<UserDto> users) {
		this.initialize();
		this.divideAndSortUsersByDebt(users);

		for (int i = 0; i < usersOwedMoney.size(); i++) {
			this.userOwedMoney = usersOwedMoney.get(i);
			for (int j=0; j < usersOwingMoney.size() && userOwedMoney.getDebt() < 0; j++) {
				this.userOwingMoney = usersOwingMoney.get(j);
				this.compensateDebt();	
				usersOwingMoney.set(j, userOwingMoney);
			}
			for (UserDto user : usersOwingMoney) if (user.getDebt()==0) usersOwingMoney.remove(user);
		}
		return this.movements;
	}
	
	private void divideAndSortUsersByDebt(List<UserDto> users) {
		for (UserDto user : users) 
			if (user.getDebt() != 0) 
				if (user.getDebt() < 0) usersOwedMoney.add(user); 
				else usersOwingMoney.add(user);
		usersOwedMoney.sort(compareUsersByDebt);
		usersOwingMoney.sort(compareUsersByDebt);
	}
	
	private void compensateDebt() {
		BigDecimal transferAmount = 
				Math.abs(userOwedMoney.getDebt()) < Math.abs(userOwingMoney.getDebt()) ?
						userOwedMoney.getBDDebt().abs() : userOwingMoney.getBDDebt().abs();
		userOwedMoney.setDebt(userOwedMoney.getBDDebt().add(transferAmount).doubleValue());
		userOwingMoney.setDebt(userOwingMoney.getBDDebt().subtract(transferAmount).doubleValue());
		this.movements.add(new MoneyMovementDto(userOwedMoney.getId(), transferAmount, userOwingMoney.getId()));
	}
	
	public BigDecimal calculatePayerDebt(BigDecimal payerUserCurrentDebt, BigDecimal price, BigDecimal userNumber) {
		return payerUserCurrentDebt.subtract(
				price.subtract(price.divide(userNumber, 2, RoundingMode.FLOOR)));
	}
	
	public BigDecimal calculateOwerDebt(BigDecimal owerCurrentDebt, BigDecimal price, BigDecimal userNumber, boolean addRoundignErrorCent) {
		BigDecimal resultDebt = owerCurrentDebt.add(
				price.divide(userNumber, 2, RoundingMode.FLOOR));
		if (addRoundignErrorCent) 
			resultDebt = resultDebt.add(BigDecimal.valueOf(0.01));
		return resultDebt;
	}

}
