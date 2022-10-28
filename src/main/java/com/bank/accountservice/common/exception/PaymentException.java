package com.bank.accountservice.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created By Shameera.A on 10/28/2022
 */
@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class PaymentException extends RuntimeException {

	public PaymentException(String message) {
		super(message);
	}
}
