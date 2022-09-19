package sharedPayments.model.dto;

import java.math.BigDecimal;

public class MoneyMovementDto {

	private Long userOwedMoneyId;
	private BigDecimal amount;
	private Long userOwingMoneyId;

	public MoneyMovementDto(Long userOwedMoneyId, BigDecimal amount, Long userOwingMoneyId) {
		super();
		this.userOwedMoneyId = userOwedMoneyId;
		this.amount = amount.setScale(2);
		this.userOwingMoneyId = userOwingMoneyId;
	}

	public Long getUserOwedMoneyId() {
		return userOwedMoneyId;
	}

	public void setUserOwedMoneyId(Long userOwedMoneyId) {
		this.userOwedMoneyId = userOwedMoneyId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getUserOwingMoneyId() {
		return userOwingMoneyId;
	}

	public void setUserOwingMoneyId(Long userOwingMoneyId) {
		this.userOwingMoneyId = userOwingMoneyId;
	}

	@Override
	public String toString() {
		return "MoneyMovementDto [userOwedMoneyId=" + userOwedMoneyId + ", amount=" + amount + ", userOwingMoneyId="
				+ userOwingMoneyId + "]";
	}

}
