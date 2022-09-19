package sharedPayments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sharedPayments.model.dto.MoneyMovementDto;
import sharedPayments.model.dto.UserDto;
import sharedPayments.model.utils.DebtCalculator;

public class DebtCalculatorTest {
	
	private DebtCalculator calculator;
	
	@BeforeEach() 
	void before () {
		this.calculator = new DebtCalculator();
		
	}
	
	@Test
	void givenZeroUsers_WhenCalculatePayerDebt_ThenDebtIsUnchanged() {
		BigDecimal userNumber = new BigDecimal(0L);
		BigDecimal owerDebt = new BigDecimal(200L);
		BigDecimal price = new BigDecimal(500L);
		
		BigDecimal result = calculator.calculatePayerDebt(owerDebt, price, userNumber);
		assertEquals("200.00", result.toString());
	}
	
	@Test
	void givenOneUser_WhenCalculatePayerDebt_ThenDebtIsUnchanged () {
		BigDecimal userNumber = new BigDecimal(1L);
		BigDecimal payerDebt = new BigDecimal(50L);
		BigDecimal price = new BigDecimal(100L);
		
		BigDecimal result = calculator.calculatePayerDebt(payerDebt, price, userNumber);
		assertEquals("50.00", result.toString());
	}
	
	@Test
	void givenTwoUsers_WhenCalculatePayerDebt_ThenDebtIsReducedByHalfPrice () {
		BigDecimal userNumber = new BigDecimal(2L);
		BigDecimal payerDebt = new BigDecimal(0L);
		BigDecimal price = new BigDecimal(100L);
		
		BigDecimal result = calculator.calculatePayerDebt(payerDebt, price, userNumber);
		assertEquals("-50.00", result.toString());
	}
	
	@Test
	void givenTwoUsers_WhenCalculatePayerDebtWithPriceOfZero_ThenDebtIsUnchanged () {
		BigDecimal userNumber = new BigDecimal(2L);
		BigDecimal payerDebt = new BigDecimal(-30L);
		BigDecimal price = new BigDecimal(0L);
		
		BigDecimal result = calculator.calculatePayerDebt(payerDebt, price, userNumber);
		assertEquals("-30.00", result.toString());		
	}
	
	@Test
	void givenThreeUsers_WhenCalculatePayerDebtWithIndivisiblePrice_ThenDebtRoundsUp () {
		BigDecimal userNumber = new BigDecimal(3L);
		BigDecimal payerDebt = new BigDecimal(0L);
		BigDecimal price = new BigDecimal(100L);
		
		BigDecimal result = calculator.calculatePayerDebt(payerDebt, price, userNumber);
		assertEquals("-66.67", result.toString());
	}

	@Test
	void givenZeroUsers_WhenCalculateOwerDebt_ThenDebtIsUnchanged() {
		BigDecimal userNumber = new BigDecimal(0L);
		BigDecimal owerDebt = new BigDecimal(200L);
		BigDecimal price = new BigDecimal(500L);
		
		BigDecimal result = calculator.calculateOwerDebt(owerDebt, price, userNumber, true);
		assertEquals("200.00", result.toString());
	}
	
	@Test
	void givenOneUser_WhenCalculateOwerDebt_ThenFullDebtIsAdded() {
		BigDecimal userNumber = new BigDecimal(1L);
		BigDecimal owerDebt = new BigDecimal(200L);
		BigDecimal price = new BigDecimal(500L);
		boolean addRoundingCent = false;
		
		BigDecimal result = calculator.calculateOwerDebt(owerDebt, price, userNumber, addRoundingCent);
		assertEquals("700.00", result.toString());
	}
	
	@Test
	void givenTwoUsers_WhenCalculateOwerDebt_ThenDebtIsIncreasedByHalfPrice() {
		BigDecimal userNumber = new BigDecimal(2L);
		BigDecimal owerDebt = new BigDecimal(75L);
		BigDecimal price = new BigDecimal(100L);
		boolean addRoundingCent = false;
		
		BigDecimal result = calculator.calculateOwerDebt(owerDebt, price, userNumber, addRoundingCent);
		assertEquals("125.00", result.toString());
	}
	
	@Test
	void givenTwoUsers_WhenCalculateOwerDebtWithPriceOfZero_ThenDebtIsUnchanged () {
		BigDecimal userNumber = new BigDecimal(2L);
		BigDecimal owerDebt = new BigDecimal(50L);
		BigDecimal price = new BigDecimal(0L);
		
		BigDecimal result = calculator.calculatePayerDebt(owerDebt, price, userNumber);
		assertEquals("50.00", result.toString());		
	}
	
	@Test
	void givenThreeUsers_WhenCalculateOwerDebtWithRoundingCent_ThenDebtIsRoundedUp() {
		BigDecimal userNumber = new BigDecimal(3L);
		BigDecimal owerDebt = new BigDecimal(0L);
		BigDecimal price = new BigDecimal(100L);
		boolean addRoundingCent = true;
		
		BigDecimal result = calculator.calculateOwerDebt(owerDebt, price, userNumber, addRoundingCent);
		assertEquals("33.34", result.toString());
	}
	
	@Test
	void givenThreeUsers_WhenCalculateOwerDebtWithNoRoundingCent_ThenDebtIsRoundedDown() {
		BigDecimal userNumber = new BigDecimal(3L);
		BigDecimal owerDebt = new BigDecimal(0L);
		BigDecimal price = new BigDecimal(100L);
		boolean addRoundingCent = false;
		
		BigDecimal result = calculator.calculateOwerDebt(owerDebt, price, userNumber, addRoundingCent);
		assertEquals("33.33", result.toString());
	}
	
	@Test
	void givenThreeUsersWithEqualDebt_WhenCalculateCompensationMovements_ThenReturnTwoMovements() {
		UserDto ower1 = new UserDto(2L, "Pepito", new BigDecimal(100));
		UserDto ower2 = new UserDto(3L, "Pepito", new BigDecimal(100));
		UserDto payer = new UserDto(1L, "Pepito", new BigDecimal(-200));
		
		List<UserDto> users = Arrays.asList(ower1, payer, ower2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==2);
	}
	
	@Test
	void givenThreeUsersWithUnequalDebt_WhenCalculateCompensationMovements_ThenMovementsCompensateToZero() {
		UserDto ower1 = new UserDto(2L, "Pepito", new BigDecimal(50));
		UserDto ower2 = new UserDto(3L, "Pepito", new BigDecimal(150));
		UserDto payer = new UserDto(1L, "Pepito", new BigDecimal(-200));
		
		List<UserDto> users = Arrays.asList(ower1, payer, ower2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==2);
		assertEquals("200.00", movements.get(0).getAmount().add(movements.get(1).getAmount()).toString());
	}

}
