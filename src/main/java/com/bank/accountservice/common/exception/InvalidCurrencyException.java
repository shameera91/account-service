package com.bank.accountservice.common.exception;

/**
 * Created By Shameera.A on 10/28/2022
 */
public class InvalidCurrencyException extends RuntimeException {

	public InvalidCurrencyException(String message) {
		super(message);
	}
}
