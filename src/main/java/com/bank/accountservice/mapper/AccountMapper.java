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

	@Select("INSERT INTO account(customer_id,country) VALUES (#{customerId},#{country}) returning id")
	long createAccount(Account account);

	@Select("SELECT * FROM account a WHERE a.id = #{id}")
	Account findAccountById(long id);

	@Select("SELECT * FROM account a WHERE a.account_id = #{accountId}")
	Account findAccountDetailsByAccountId(String accountId);
}
