package com.bank.accountservice.service;

import com.bank.accountservice.mapper.BalanceMapper;
import com.bank.accountservice.modal.Balance;
import org.springframework.stereotype.Service;

import com.bank.accountservice.mapper.AccountMapper;
import com.bank.accountservice.modal.Account;

import lombok.RequiredArgsConstructor;

/**
 * Created By Shameera.A on 10/26/2022
 */

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountMapper accountMapper;
	private final BalanceMapper balanceMapper;

	public void test(){



		Account a = new Account();
		a.setCustomerId("c324324");

		long savedId = accountMapper.insert(a);

		Balance balance = new Balance();

		balance.setAmount(1000.00);
		balance.setCurrency("USD");
		balance.setAccId(savedId);

		balanceMapper.insertBalance(balance);

		accountMapper.findAll().forEach(s-> System.out.println(s.getAccountId() + " "+ s.getId()));

		balanceMapper.findAll().forEach(bb-> System.out.println(bb.getAccId() +" "+bb.getAmount() ));



	}

	//public
}
