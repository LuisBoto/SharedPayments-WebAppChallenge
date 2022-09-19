package sharedPayments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import sharedPayments.model.utils.DebtCalculator;

public class DebtCalculatorTest {
	
	@Test
	void givenOneUser_WhenCalculatePayerDebt_ThenDebtIsUnchanged () {
		DebtCalculator calculator = new DebtCalculator();
		BigDecimal userNumber = new BigDecimal(1L);
		BigDecimal payerDebt = new BigDecimal(50L);
		BigDecimal price = new BigDecimal(100L);
		
		BigDecimal result = calculator.calculatePayerDebt(payerDebt, price, userNumber);
		assertEquals("50.00", result.toString());
	}
	
	@Test
	void givenTwoUsers_WhenCalculatePayerDebt_ThenDebtIsReducedByHalfPrice () {
		DebtCalculator calculator = new DebtCalculator();
		BigDecimal userNumber = new BigDecimal(2L);
		BigDecimal payerDebt = new BigDecimal(0L);
		BigDecimal price = new BigDecimal(100L);
		
		BigDecimal result = calculator.calculatePayerDebt(payerDebt, price, userNumber);
		assertEquals("-50.00", result.toString());
	}
	
	@Test
	void givenThreeUsers_WhenCalculatePayerDebtWithIndivisiblePrice_ThenDebtRoundsUp () {
		DebtCalculator calculator = new DebtCalculator();
		BigDecimal userNumber = new BigDecimal(3L);
		BigDecimal payerDebt = new BigDecimal(0L);
		BigDecimal price = new BigDecimal(100L);
		
		BigDecimal result = calculator.calculatePayerDebt(payerDebt, price, userNumber);
		assertEquals("-66.67", result.toString());
	}

}
