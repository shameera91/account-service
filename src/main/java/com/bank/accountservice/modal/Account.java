package com.bank.accountservice.modal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created By Shameera.A on 10/26/2022
 */
@Getter
@Setter
public class Account {

	private long id;
	private String accountId;
	private String customerId;

	private List<Balance> accountBalances;
}
