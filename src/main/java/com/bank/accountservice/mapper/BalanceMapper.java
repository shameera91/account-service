package com.bank.accountservice.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.bank.accountservice.dto.BalanceOutputDTO;
import com.bank.accountservice.modal.Balance;
import org.apache.ibatis.annotations.Update;

/**
 * Created By Shameera.A on 10/26/2022
 */

@Mapper
public interface BalanceMapper {

	@Insert("INSERT INTO balance(currency,amount,account_id) VALUES (#{currency},#{amount},#{accountId})")
	void createBalance(Balance balance);

	@Select("SELECT b.amount,b.currency FROM balance b WHERE b.account_id = #{accountId} ")
	List<BalanceOutputDTO> findBalancesByAccountId(long accountId);

	@Select("SELECT * FROM balance b WHERE b.account_id = #{accountId} ")
	List<Balance> findAllBalanceDetailsByAccountId(long accountId);

	@Update("UPDATE balance SET amount = #{amount} WHERE id = #{balanceId}")
	void updateBalance(BigDecimal amount,long balanceId);
}
