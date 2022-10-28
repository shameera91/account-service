package com.bank.accountservice.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * Created By Shameera.A on 10/28/2022
 */
@Getter
@Builder
public class AccountDetailOutputDTO {

	private String accountId;
	private String customerId;
	private List<BalanceOutputDTO> balances;
}
