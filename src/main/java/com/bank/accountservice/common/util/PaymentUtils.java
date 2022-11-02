package com.bank.accountservice.common.util;

import com.bank.accountservice.common.enums.TransactionDirection;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.util.EnumUtils;

import com.bank.accountservice.common.enums.CurrencyType;
import com.bank.accountservice.common.exception.InvalidCurrencyException;

/**
 * Created By Shameera.A on 10/28/2022
 */
@Slf4j
public class PaymentUtils {

	private PaymentUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static void validateCurrencyType(String currency) {
		try {
			EnumUtils.findEnumInsensitiveCase(CurrencyType.class, currency);
		} catch (Exception e) {
			log.error("Invalid Currency found");
			throw new InvalidCurrencyException("Invalid Currency");
		}
	}

	public static void validateTransactionDirectionType(String direction){
		try {
			EnumUtils.findEnumInsensitiveCase(TransactionDirection.class, direction);
		} catch (Exception e) {
			log.error("Invalid Transaction Direction Found");
			throw new InvalidCurrencyException("Invalid Transaction Direction");
		}
	}
}
