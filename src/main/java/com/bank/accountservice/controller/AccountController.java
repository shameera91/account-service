package com.bank.accountservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.accountservice.dto.CreateAccountInputDTO;
import com.bank.accountservice.service.AccountService;

import lombok.RequiredArgsConstructor;

/**
 * Created By Shameera.A on 10/26/2022
 */

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@PostMapping
	public ResponseEntity createAccount(@RequestBody CreateAccountInputDTO createAccountInputDTO) {
		return ResponseEntity.ok(accountService.createNewBankAccount(createAccountInputDTO));
	}

	@GetMapping("/{accountId}")
	public ResponseEntity getAccountByAccountId(@PathVariable String accountId) {
		return ResponseEntity.ok(accountService.getAccountDetailsByAccountId(accountId));
	}
}
