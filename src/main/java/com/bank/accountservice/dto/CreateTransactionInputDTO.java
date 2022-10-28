package com.bank.accountservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Created By Shameera.A on 10/28/2022
 */
@Getter
@Setter
public class CreateTransactionInputDTO {

	private String accountId;
	private BigDecimal amount;
	private String currency;
	private String direction;
	private String description;

}
