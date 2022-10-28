package com.bank.accountservice.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Created By Shameera.A on 10/28/2022
 */

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> resourceNotFoundException(ResourceNotFoundException ex) {
		log.error(ex.getMessage());
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), ex);
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidCurrencyException.class)
	public ResponseEntity<ApiError> invalidCurrencyException(Exception ex) {
		log.error(ex.getMessage());
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ApiError> paymentException(Exception ex) {
		log.error(ex.getMessage());
		ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage());
		return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
	}

	// 500 - internal server error
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> exceptionHandler(Exception ex) {
		log.error(ex.getMessage());
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Something went wrong. Please contact System Admin");
		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
