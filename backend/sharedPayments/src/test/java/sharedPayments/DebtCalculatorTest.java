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
	void givenOneUserWithDebt_WhenCalculateMovements_ThenNoMovements() {
		UserDto ower1 = new UserDto(1L, "Pepito", new BigDecimal(50));
		
		List<UserDto> users = Arrays.asList(ower1);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==0);
	}
	
	@Test
	void givenOneUserWithPositiveDebt_WhenCalculateMovements_ThenNoMovements() {
		UserDto ower1 = new UserDto(1L, "Pepito", new BigDecimal(-50));
		
		List<UserDto> users = Arrays.asList(ower1);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==0);
	}
	
	@Test
	void givenTwoUsersWithDebt_WhenCalculateMovements_ThenNoMovements() {
		UserDto ower1 = new UserDto(1L, "Pepito", new BigDecimal(100));
		UserDto ower2 = new UserDto(2L, "Pepita", new BigDecimal(100));
		
		List<UserDto> users = Arrays.asList(ower1, ower2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==0);
	}
	
	@Test
	void givenTwoUsersWithPositiveDebt_WhenCalculateMovements_ThenNoMovements() {
		UserDto ower1 = new UserDto(1L, "Pepito", new BigDecimal(-100));
		UserDto ower2 = new UserDto(2L, "Pepita", new BigDecimal(-100));
		
		List<UserDto> users = Arrays.asList(ower1, ower2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==0);
	}
	
	@Test
	void givenTwoUsersWithComplementaryDebt_WhenCalculateMovements_ThenFullAmountSingleMovement () {
		UserDto ower1 = new UserDto(1L, "Pepito", new BigDecimal(-100));
		UserDto ower2 = new UserDto(2L, "Pepita", new BigDecimal(100));
		
		List<UserDto> users = Arrays.asList(ower1, ower2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==1);
		assertEquals("100.00", movements.get(0).getAmount().toString());
	}
	
	@Test
	void givenTwoUsersWithComplementaryDebt_WhenCalculateMovements_ThenOnePaysTheOther () {
		UserDto ower1 = new UserDto(1L, "Pepito", new BigDecimal(-100));
		UserDto ower2 = new UserDto(2L, "Pepita", new BigDecimal(100));
		
		List<UserDto> users = Arrays.asList(ower1, ower2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==1);
		assertEquals(2L, movements.get(0).getUserOwingMoneyId());
		assertEquals(1L, movements.get(0).getUserOwedMoneyId());
	}
	
	@Test
	void givenThreeUsersWithEqualDebt_WhenCalculateMovements_ThenReturnTwoMovements() {
		UserDto ower1 = new UserDto(2L, "Pepito", new BigDecimal(100));
		UserDto ower2 = new UserDto(3L, "Pepito", new BigDecimal(100));
		UserDto payer = new UserDto(1L, "Pepito", new BigDecimal(-200));
		
		List<UserDto> users = Arrays.asList(ower1, payer, ower2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==2);
	}
	
	@Test
	void givenThreeUsersWithUnequalDebt_WhenCalculateMovements_ThenMovementsCompensateToZero() {
		UserDto ower1 = new UserDto(2L, "Pepito", new BigDecimal(50));
		UserDto ower2 = new UserDto(3L, "Pepito", new BigDecimal(150));
		UserDto payer = new UserDto(1L, "Pepito", new BigDecimal(-200));
		
		List<UserDto> users = Arrays.asList(ower1, payer, ower2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==2);
		assertEquals("200.00", movements.get(0).getAmount().add(movements.get(1).getAmount()).toString());
	}
	
	@Test
	void givenThreeUsersWithUnequalDebt_WhenCalculateMovements_ThenDebtorPaysTheirFullDebt() {
		UserDto ower1 = new UserDto(1L, "Pepito", new BigDecimal(500));
		UserDto payer1 = new UserDto(2L, "Juanito", new BigDecimal(-300));
		UserDto payer2 = new UserDto(3L, "Joselito", new BigDecimal(-400));
		
		List<UserDto> users = Arrays.asList(ower1, payer1, payer2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==2);
		assertEquals("300.00", movements.get(0).getAmount().toString());
		assertEquals("200.00", movements.get(1).getAmount().toString());
	}
	
	@Test
	void givenThreeUsersWithUnequalDebt_WhenCalculateMovements_ThenCreditorsReceiveMoney() {
		UserDto ower1 = new UserDto(1L, "Pepito", new BigDecimal(80));
		UserDto payer1 = new UserDto(2L, "Juanito", new BigDecimal(-30));
		UserDto payer2 = new UserDto(3L, "Joselito", new BigDecimal(-60));
		
		List<UserDto> users = Arrays.asList(ower1, payer1, payer2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==2);
		assertEquals(2L, movements.get(0).getUserOwedMoneyId());
		assertEquals(3L, movements.get(1).getUserOwedMoneyId());
	}
	
	@Test
	void givenFourUsersWithUnequalDebt_WhenCalculateMovements_ThenAllDebtorsPayTheirFullDebt() {
		UserDto ower1 = new UserDto(1L, "Pepita", new BigDecimal(500));
		UserDto ower2 = new UserDto(2L, "Juanito", new BigDecimal(900));
		UserDto payer1 = new UserDto(3L, "Joselito", new BigDecimal(-1000));
		UserDto payer2 = new UserDto(3L, "Manolita", new BigDecimal(-600));
		
		List<UserDto> users = Arrays.asList(ower1, ower2, payer1, payer2);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==3);
		assertEquals("600.00", movements.get(0).getAmount().toString());
		assertEquals("300.00", movements.get(1).getAmount().toString());
		assertEquals("500.00", movements.get(2).getAmount().toString());
	}
	
	@Test
	void givenFourUsersWithDecimalDebt_WhenCalculateMovements_ThenMovementsCompensateToZero() {
		UserDto ower1 = new UserDto(1L, "Pepita", new BigDecimal(45.05));
		UserDto ower2 = new UserDto(2L, "Juanito", new BigDecimal(67.89));
		UserDto ower3 = new UserDto(3L, "Joselito", new BigDecimal(24.35));
		UserDto payer1 = new UserDto(4L, "Manolita", new BigDecimal(-137.29));
		
		List<UserDto> users = Arrays.asList(ower1, ower2, payer1, ower3);
		List<MoneyMovementDto> movements = this.calculator.calculateCompensationMovements(users);
		assertTrue(movements.size()==3);
		String result = movements.get(0).getAmount()
				.add(movements.get(1).getAmount())
				.add(movements.get(2).getAmount()).toString();
		assertEquals("137.29", result);
	}


}
