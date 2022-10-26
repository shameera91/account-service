package com.bank.accountservice.modal;

import lombok.Getter;
import lombok.Setter;

/**
 * Created By Shameera.A on 10/26/2022
 */

@Getter
@Setter
public class Balance {

	private long id;
	private String currency;
	private double amount;

	//used as the foreign key of this table
	private long accId;
}
