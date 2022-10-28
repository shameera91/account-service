package com.bank.accountservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.accountservice.dto.CreateTransactionInputDTO;
import com.bank.accountservice.service.TransactionService;

import lombok.RequiredArgsConstructor;

/**
 * Created By Shameera.A on 10/28/2022
 */

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@PostMapping
	public ResponseEntity createTransaction(@RequestBody CreateTransactionInputDTO createTransactionInputDTO) {
		return ResponseEntity.ok(transactionService.createTransaction(createTransactionInputDTO));
	}

	@GetMapping("/{accountId}")
	public ResponseEntity getAllTransactionsByAccountId(@PathVariable String accountId) {
		return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountId));
	}
}
