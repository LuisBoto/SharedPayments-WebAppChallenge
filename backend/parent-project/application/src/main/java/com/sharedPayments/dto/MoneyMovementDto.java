package com.sharedPayments.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Data;

@Data
public class MoneyMovementDto {

	private Long userOwedMoneyId;
	private BigDecimal amount;
	private Long userOwingMoneyId;

	public MoneyMovementDto(Long userOwedMoneyId, BigDecimal amount, Long userOwingMoneyId) {
		this.userOwedMoneyId = userOwedMoneyId;
		this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
		this.userOwingMoneyId = userOwingMoneyId;
	}

}
