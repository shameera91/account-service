package com.bank.accountservice.modal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created By Shameera.A on 10/26/2022
 */
@Getter
@Setter
@Builder
public class Account {

	private long id;
	private String accountId;
	private String customerId;
	private String country;
}
