package com.bank.accountservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bank.accountservice.dto.BalanceOutputDTO;
import com.bank.accountservice.modal.Balance;

/**
 * Created By Shameera.A on 10/26/2022
 */

@Mapper
public interface BalanceMapper {

	@Insert("INSERT INTO balance(currency,amount,account_id) VALUES (#{currency},#{amount},#{accountId})")
	void createBalance(Balance balance);

	@Select("SELECT * FROM balance")
	List<Balance> findAll();

	@Select("SELECT b.amount,b.currency FROM balance b WHERE b.account_id = #{accountId} ")
	List<BalanceOutputDTO> findBalancesByAccountId(long accountId);
}
