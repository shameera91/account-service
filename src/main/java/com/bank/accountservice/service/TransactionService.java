package com.bank.accountservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bank.accountservice.common.enums.TransactionDirection;
import com.bank.accountservice.common.exception.PaymentException;
import com.bank.accountservice.common.exception.ResourceNotFoundException;
import com.bank.accountservice.common.util.PaymentUtils;
import com.bank.accountservice.dto.CreateTransactionInputDTO;
import com.bank.accountservice.dto.TransactionDetailOutputDTO;
import com.bank.accountservice.dto.TransactionsOutputDTO;
import com.bank.accountservice.mapper.AccountMapper;
import com.bank.accountservice.mapper.BalanceMapper;
import com.bank.accountservice.mapper.TransactionsMapper;
import com.bank.accountservice.modal.Account;
import com.bank.accountservice.modal.Balance;
import com.bank.accountservice.modal.Transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created By Shameera.A on 10/28/2022
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

	private final AccountMapper accountMapper;
	private final BalanceMapper balanceMapper;
	private final TransactionsMapper transactionsMapper;

	//TODO publish insert and update operations in rabbbit mq

	@Transactional
	public TransactionDetailOutputDTO createTransaction(CreateTransactionInputDTO inputDTO) {

		PaymentUtils.validateCurrencyType(inputDTO.getCurrency());

		PaymentUtils.validateTransactionDirectionType(inputDTO.getDirection());

		if (inputDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new PaymentException("Transaction Amount can not be negative");
		}

		if (!StringUtils.hasText(inputDTO.getDescription())) {
			throw new PaymentException("Description can not be empty");
		}

		Account account = getAccountDetailsByAccountId(inputDTO.getAccountId());

		Transaction transaction = Transaction.builder().accountId(account.getId()).amount(inputDTO.getAmount())
				.currency(inputDTO.getCurrency()).description(inputDTO.getDescription())
				.direction(inputDTO.getDirection()).build();

		BigDecimal balanceAmount = calculateAndAdjustBalance(account, transaction);

		log.info("Adjust Balances and update account details completed");

		long savedTransactionId = transactionsMapper.createTransaction(transaction);
		log.info("Transaction Created Successfully");

		Transaction savedTransaction = transactionsMapper.findTransactionById(savedTransactionId);

		return TransactionDetailOutputDTO.builder().accountId(account.getAccountId())
				.transactionId(savedTransaction.getTransactionId()).amount(savedTransaction.getAmount())
				.currency(savedTransaction.getCurrency()).direction(savedTransaction.getDirection())
				.description(savedTransaction.getDescription()).balanceAmount(balanceAmount).build();
	}

	private BigDecimal calculateAndAdjustBalance(Account account, Transaction transaction) {
		log.info("Adjust Balances and update account details initiated");
		List<Balance> balancesByAccountId = balanceMapper.findAllBalanceDetailsByAccountId(account.getId());

		Balance balanceObject = balancesByAccountId.stream()
				.filter(balance -> balance.getCurrency().equals(transaction.getCurrency())).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("No Balance Object Found For Given Currency Type"));

		if (transaction.getDirection().equals(TransactionDirection.OUT.name())) {

			if (balanceObject.getAmount().compareTo(transaction.getAmount()) < 0) {
				throw new PaymentException("Insufficient funds to perform the transaction");
			}

			return updateBalance(balanceObject, balanceObject.getAmount().subtract(transaction.getAmount()));

		} else {

			return updateBalance(balanceObject, balanceObject.getAmount().add(transaction.getAmount()));
		}
	}

	private BigDecimal updateBalance(Balance balanceObject, BigDecimal balanceAmount) {
		balanceMapper.updateBalance(balanceAmount, balanceObject.getId());
		return balanceAmount;
	}

	public List<TransactionsOutputDTO> getTransactionsByAccountNumber(String accountId) {
		log.info("Get transaction details by account id initiated");
		Account account = getAccountDetailsByAccountId(accountId);

		List<Transaction> transactionList = transactionsMapper.findTransactionsByAccountId(account.getId());

		List<TransactionsOutputDTO> result = new ArrayList<>();
		transactionList.forEach(t -> result.add(TransactionsOutputDTO.builder().accountId(accountId)
				.amount(t.getAmount()).currency(t.getCurrency()).description(t.getDescription())
				.direction(t.getDirection()).transactionId(t.getTransactionId()).build()));
		return result;
	}

	private Account getAccountDetailsByAccountId(String accountId) {
		Account account = accountMapper.findAccountDetailsByAccountId(accountId);

		if (Objects.isNull(account)) {
			throw new ResourceNotFoundException("No account found for account id: " + accountId);
		}
		return account;
	}
}
