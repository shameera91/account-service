package com.bank.accountservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.bank.accountservice.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import com.bank.accountservice.common.enums.CurrencyType;
import com.bank.accountservice.common.exception.InvalidCurrencyException;
import com.bank.accountservice.dto.AccountDetailOutputDTO;
import com.bank.accountservice.dto.BalanceOutputDTO;
import com.bank.accountservice.dto.CreateAccountInputDTO;
import com.bank.accountservice.mapper.AccountMapper;
import com.bank.accountservice.mapper.BalanceMapper;
import com.bank.accountservice.modal.Account;
import com.bank.accountservice.modal.Balance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created By Shameera.A on 10/26/2022
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountMapper accountMapper;
	private final BalanceMapper balanceMapper;

	public AccountDetailOutputDTO createNewBankAccount(CreateAccountInputDTO createAccountInputDTO) {

		createAccountInputDTO.getCurrencyTypes().forEach(currency -> {
			try {
				EnumUtils.findEnumInsensitiveCase(CurrencyType.class, currency);
			} catch (Exception e) {
				throw new InvalidCurrencyException("Invalid Currency");
			}
		});

		log.info("Initiate create new account for customer id: " + createAccountInputDTO.getCustomerId());

		Account account = Account.builder().customerId(createAccountInputDTO.getCustomerId()).build();
		long savedAccountId = accountMapper.createAccount(account);

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
		Account account = accountMapper.findAccountDetailsByAccountId(accountId);

		if(Objects.isNull(account)){
			throw new ResourceNotFoundException("No account found for account id: "+accountId);
		}
		List<BalanceOutputDTO> balancesByAccountId = balanceMapper.findBalancesByAccountId(account.getId());

		return AccountDetailOutputDTO.builder().customerId(account.getCustomerId()).accountId(account.getAccountId())
				.balances(balancesByAccountId).build();
	}
}
