package com.bank.accountservice.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created By Shameera.A on 10/28/2022
 */

@Setter
@Getter
public class ApiError {

	private int status;
	private String message;

	ApiError() {
	}

	ApiError(int status) {
		this.status = status;
	}

	ApiError(int status, Throwable ex) {
		this.status = status;
		this.message = ex.getLocalizedMessage();
	}

	ApiError(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
