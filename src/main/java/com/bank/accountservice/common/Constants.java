package com.bank.accountservice.common;

/**
 * Created By Shameera.A on 11/2/2022
 */
public class Constants {

	private Constants() {
		throw new IllegalStateException("Utility class");
	}

	public static final String ACCOUNT_QUEUE = "account_queue";
	public static final String TRANSACTION_QUEUE = "transaction_queue";
	public static final String EXCHANGE = "rabbit_exchange";
	public static final String ACCOUNT_ROUTING_KEY = "account_routingKey";
	public static final String TRANSACTION_ROUTING_KEY = "transaction_routingKey";
}
