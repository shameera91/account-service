package com.bank.accountservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bank.accountservice.modal.Account;

/**
 * Created By Shameera.A on 10/26/2022
 */

@Mapper
public interface AccountMapper {

	@Select("SELECT * FROM account")
	List<Account> findAll();

	@Select("INSERT INTO account(customer_id) VALUES (#{customerId}) returning id")
	long insert(Account account);
}
