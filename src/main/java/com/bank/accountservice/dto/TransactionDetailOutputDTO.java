package com.bank.accountservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Created By Shameera.A on 10/28/2022
 */
@Getter
@Builder
public class TransactionDetailOutputDTO {

	private String accountId;
	private String transactionId;
	private BigDecimal amount;
	private String currency;
	private String direction;
	private String description;
	private BigDecimal balanceAmount;
}
