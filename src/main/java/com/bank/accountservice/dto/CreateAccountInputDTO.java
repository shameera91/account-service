package com.bank.accountservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Created By Shameera.A on 10/28/2022
 */

@Getter
@Setter
public class CreateAccountInputDTO {

	private String customerId;
	private String country;

	private List<String> currencyTypes;

	@JsonCreator
	public CreateAccountInputDTO(@JsonProperty("customerId") String customerId, @JsonProperty("country") String country,
			@JsonProperty("currencyTypes") List<String> currencyTypes) {
		this.customerId = customerId;
		this.country = country;
		this.currencyTypes = currencyTypes;
	}
}
