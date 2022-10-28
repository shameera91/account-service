package com.bank.accountservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bank.accountservice.modal.Transaction;

import java.util.List;

/**
 * Created By Shameera.A on 10/27/2022
 */

@Mapper
public interface TransactionsMapper {

	@Select("INSERT INTO transactions(description,direction,currency,amount,account_id) "
			+ "VALUES (#{description},#{direction},#{currency},#{amount},#{accountId}) returning id")
	long createTransaction(Transaction transaction);

	@Select("SELECT * FROM transactions t WHERE t.id = #{id}")
	Transaction findTransactionById(long id);

	@Select("SELECT * FROM transactions t WHERE t.account_id = #{accountId}")
	List<Transaction> findTransactionsByAccountId(long accountId);

}
