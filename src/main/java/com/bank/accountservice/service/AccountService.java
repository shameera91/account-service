package com.bank.accountservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.bank.accountservice.common.exception.ResourceNotFoundException;
import com.bank.accountservice.common.util.PaymentUtils;
import com.bank.accountservice.dto.AccountDetailOutputDTO;
import com.bank.accountservice.dto.BalanceOutputDTO;
import com.bank.accountservice.dto.CreateAccountInputDTO;
import com.bank.accountservice.mapper.AccountMapper;
import com.bank.accountservice.mapper.BalanceMapper;
import com.bank.accountservice.modal.Account;
import com.bank.accountservice.modal.Balance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created By Shameera.A on 10/26/2022
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountMapper accountMapper;
	private final BalanceMapper balanceMapper;

	@Transactional
	public AccountDetailOutputDTO createNewBankAccount(CreateAccountInputDTO createAccountInputDTO) {

		createAccountInputDTO.getCurrencyTypes().forEach(PaymentUtils::validateCurrencyType);

		log.info("Initiate create new account for customer id: " + createAccountInputDTO.getCustomerId());

		Account account = Account.builder().customerId(createAccountInputDTO.getCustomerId()).build();

		long savedAccountId = accountMapper.createAccount(account);

		log.info("Created new account with id : " + savedAccountId);

		createAccountInputDTO.getCurrencyTypes().forEach(t -> {
			Balance balance = Balance.builder().currency(t).accountId(savedAccountId).amount(BigDecimal.ZERO).build();
			balanceMapper.createBalance(balance);
		});

		Account savedAccount = accountMapper.findAccountById(savedAccountId);

		List<BalanceOutputDTO> balancesByAccountId = balanceMapper.findBalancesByAccountId(savedAccountId);

		return AccountDetailOutputDTO.builder().customerId(savedAccount.getCustomerId())
				.accountId(savedAccount.getAccountId()).balances(balancesByAccountId).build();
	}

	public AccountDetailOutputDTO getAccountDetailsByAccountId(String accountId) {
		log.info("Fetching details  initiated for account id :" + accountId);
		Account account = accountMapper.findAccountDetailsByAccountId(accountId);

		if (Objects.isNull(account)) {
			throw new ResourceNotFoundException("No account found for account id: " + accountId);
		}
		List<BalanceOutputDTO> balancesByAccountId = balanceMapper.findBalancesByAccountId(account.getId());

		log.info("Fetching details for account id {} successful", account);
		return AccountDetailOutputDTO.builder().customerId(account.getCustomerId()).accountId(account.getAccountId())
				.balances(balancesByAccountId).build();
	}
}
