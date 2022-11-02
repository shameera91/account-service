package com.bank.accountservice.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
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

	@JsonCreator
	public CreateTransactionInputDTO(@JsonProperty("accountId") String accountId,
			@JsonProperty("amount") BigDecimal amount, @JsonProperty("currency") String currency,
			@JsonProperty("direction") String direction, @JsonProperty("description") String description) {
		this.accountId = accountId;
		this.amount = amount;
		this.currency = currency;
		this.direction = direction;
		this.description = description;
	}
}
