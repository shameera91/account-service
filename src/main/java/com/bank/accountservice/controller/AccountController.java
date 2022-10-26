package com.bank.accountservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping
	public void getResp(){
		accountService.test();
	}
}
