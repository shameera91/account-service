package com.bank.accountservice.account;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bank.accountservice.dto.CreateAccountInputDTO;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;

/**
 * Created By Shameera.A on 11/2/2022
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountITest {

	private static final String LOCAL_HOST = "http://localhost";
	public static final String ACCOUNT_RESOURCE = "/api/v1/account";
	public static final String ACCOUNT_BY_ID_RESOURCE = "/api/v1/account/{accountId}";
	private static final String CLIENT_ID = "CA0011";
	private static final String COUNTRY = "Estonia";
	private static final List<String> currencyList = List.of("EUR", "SEK", "GBP");

	private CreateAccountInputDTO createAccountInputDTO;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = LOCAL_HOST;
		RestAssured.port = port;

		createAccountInputDTO = new CreateAccountInputDTO(CLIENT_ID, COUNTRY, currencyList);
	}

	@AfterEach
	public void cleanUp() {

	}

	@Test
	public void testCreateAccount() {
		RestAssured.given().contentType(ContentType.JSON).body(createAccountInputDTO).post(ACCOUNT_RESOURCE).then()
				.statusCode(HttpStatus.SC_OK).body("customerId", equalTo(CLIENT_ID));
	}

	@Test
	public void testCreateAccountWithInvalidCurrency() {
		CreateAccountInputDTO createAccountWithInvalidCurrency = new CreateAccountInputDTO(CLIENT_ID, COUNTRY,
				List.of("USD", "EUR", "LKR"));
		RestAssured.given().contentType(ContentType.JSON).body(createAccountWithInvalidCurrency).post(ACCOUNT_RESOURCE)
				.then().statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void testGetAccountByAccountId() {
		String responseString = RestAssured.given().contentType(ContentType.JSON).body(createAccountInputDTO)
				.post(ACCOUNT_RESOURCE).then().statusCode(HttpStatus.SC_OK).extract().asString();

		String accountId = JsonPath.from(responseString).getString("accountId");

		RestAssured.given().contentType(ContentType.JSON).pathParam("accountId", accountId).get(ACCOUNT_BY_ID_RESOURCE)
				.then().statusCode(HttpStatus.SC_OK).body("accountId", equalTo(accountId));

	}

	@Test
	public void testGetAccountWithIncorrectAccountId() {
		RestAssured.given().contentType(ContentType.JSON).body(createAccountInputDTO).post(ACCOUNT_RESOURCE).then()
				.statusCode(HttpStatus.SC_OK).extract().asString();

		RestAssured.given().contentType(ContentType.JSON).pathParam("accountId", "CA-081-6653")
				.get(ACCOUNT_BY_ID_RESOURCE).then().statusCode(HttpStatus.SC_NOT_FOUND);

	}

}
