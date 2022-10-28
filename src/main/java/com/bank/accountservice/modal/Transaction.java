package com.bank.accountservice.modal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created By Shameera.A on 10/26/2022
 */
@Getter
@Setter
@Builder
public class Transaction {

	private long id;
	private String transactionId;
	private String description;
	private String direction;
	private String currency;
	private BigDecimal amount;

	private long accountId;

}
