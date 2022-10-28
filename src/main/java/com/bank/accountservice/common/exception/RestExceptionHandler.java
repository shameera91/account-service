package com.bank.accountservice.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created By Shameera.A on 10/28/2022
 */

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> methodArgsNotValidExceptions(MethodArgumentNotValidException ex) {
		StringBuilder sb = new StringBuilder();
		ex.getBindingResult().getAllErrors().forEach(error -> sb.append("In field ")
				.append(((FieldError) error).getField()).append(", ").append(error.getDefaultMessage()).append(".  "));
		log.error(String.format("Method arguments are not valid. %s", sb.toString()));
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), sb.toString());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

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
}
