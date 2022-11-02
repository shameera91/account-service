package com.bank.accountservice.transactions;

import static org.hamcrest.CoreMatchers.equalTo;

import java.math.BigDecimal;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bank.accountservice.common.enums.CurrencyType;
import com.bank.accountservice.common.enums.TransactionDirection;
import com.bank.accountservice.dto.CreateAccountInputDTO;
import com.bank.accountservice.dto.CreateTransactionInputDTO;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

/**
 * Created By Shameera.A on 11/2/2022
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionITest {

	private static final String LOCAL_HOST = "http://localhost";
	public static final String TRANSACTION_RESOURCE = "/api/v1/transaction";
	public static final String TRANSACTIONS_BY_ACCOUNT_ID_RESOURCE = "/api/v1/transaction/{accountId}";
	public static final String ACCOUNT_RESOURCE = "/api/v1/account";
	private static final String ACCOUNT_ID_ONE = "CA-081-1";
	public static final String DESCRIPTION = "Test Description";
	public static final BigDecimal TRANSACTION_AMOUNT = BigDecimal.valueOf(1000.00);
	public static final BigDecimal DEBIT_TRANSACTION_AMOUNT = BigDecimal.valueOf(550.00);
	public static final String CURRENCY = CurrencyType.EUR.name();
	public static final String DIRECTION = TransactionDirection.IN.name();
	private static final String CLIENT_ID = "CA0011";
	private static final String COUNTRY = "Estonia";
	private static final List<String> currencyList = List.of("EUR", "SEK", "GBP");
	private CreateTransactionInputDTO createTransactionInputDTO;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = LOCAL_HOST;
		RestAssured.port = port;

		CreateAccountInputDTO createAccountInputDTO = new CreateAccountInputDTO(CLIENT_ID, COUNTRY, currencyList);

		RestAssured.given().contentType(ContentType.JSON).body(createAccountInputDTO).post(ACCOUNT_RESOURCE).then()
				.statusCode(HttpStatus.SC_OK).body("customerId", equalTo(CLIENT_ID)).extract().asString();

		createTransactionInputDTO = new CreateTransactionInputDTO(ACCOUNT_ID_ONE, TRANSACTION_AMOUNT, CURRENCY,
				DIRECTION, DESCRIPTION);
	}

	@AfterEach
	public void cleanUp() {

	}

	@Test
	public void testCreateTransaction() {
		RestAssured.given().contentType(ContentType.JSON).body(createTransactionInputDTO).post(TRANSACTION_RESOURCE)
				.then().statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void testCreateTransactionWithInvalidCurrency() {

		createTransactionInputDTO = new CreateTransactionInputDTO(ACCOUNT_ID_ONE, TRANSACTION_AMOUNT, "AUD", DIRECTION,
				DESCRIPTION);

		RestAssured.given().contentType(ContentType.JSON).body(createTransactionInputDTO).post(TRANSACTION_RESOURCE)
				.then().statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void testCreateTransactionWithInvalidTransactionDirection() {

		createTransactionInputDTO = new CreateTransactionInputDTO(ACCOUNT_ID_ONE, TRANSACTION_AMOUNT, CURRENCY, "INC",
				DESCRIPTION);

		RestAssured.given().contentType(ContentType.JSON).body(createTransactionInputDTO).post(TRANSACTION_RESOURCE)
				.then().statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void testCreateTransactionWithInvalidTransactionAmount() {

		createTransactionInputDTO = new CreateTransactionInputDTO(ACCOUNT_ID_ONE, BigDecimal.valueOf(-50.00), CURRENCY,
				DIRECTION, DESCRIPTION);

		RestAssured.given().contentType(ContentType.JSON).body(createTransactionInputDTO).post(TRANSACTION_RESOURCE)
				.then().statusCode(HttpStatus.SC_NOT_ACCEPTABLE);
	}

	@Test
	public void testCreateDebitTransactionWhenInsufficientFundsAvailable() {

		RestAssured.given().contentType(ContentType.JSON).body(createTransactionInputDTO).post(TRANSACTION_RESOURCE)
				.then().statusCode(HttpStatus.SC_OK);

		// perform debit transaction
		CreateTransactionInputDTO debitTxnInput = new CreateTransactionInputDTO(ACCOUNT_ID_ONE,
				BigDecimal.valueOf(5000.00), CURRENCY, TransactionDirection.OUT.name(), DESCRIPTION);

		RestAssured.given().contentType(ContentType.JSON).body(debitTxnInput).post(TRANSACTION_RESOURCE).then()
				.statusCode(HttpStatus.SC_NOT_ACCEPTABLE);

	}

	@Test
	public void testCreateTransactionWhenNoSuchAccountAvailable() {

		CreateTransactionInputDTO createTransactionInput = createTransactionInputDTO = new CreateTransactionInputDTO(
				"CA-081-88654", TRANSACTION_AMOUNT, CURRENCY, DIRECTION, DESCRIPTION);

		RestAssured.given().contentType(ContentType.JSON).body(createTransactionInput).post(TRANSACTION_RESOURCE).then()
				.statusCode(HttpStatus.SC_NOT_FOUND);

	}

	@Test
	public void testCreateTransactionWhenNoDescriptionAvailable() {

		CreateTransactionInputDTO createTransactionInput = createTransactionInputDTO = new CreateTransactionInputDTO(
				ACCOUNT_ID_ONE, TRANSACTION_AMOUNT, CURRENCY, DIRECTION, "");

		RestAssured.given().contentType(ContentType.JSON).body(createTransactionInput).post(TRANSACTION_RESOURCE).then()
				.statusCode(HttpStatus.SC_NOT_ACCEPTABLE);

	}

	@Test
	public void testGetTransactionsByAccountId() {

		performTransactions();

		RestAssured.given().contentType(ContentType.JSON).pathParam("accountId", ACCOUNT_ID_ONE)
				.get(TRANSACTIONS_BY_ACCOUNT_ID_RESOURCE).then().statusCode(HttpStatus.SC_OK)
				.body("[0].accountId", equalTo(ACCOUNT_ID_ONE)).body("[0].transactionId", equalTo("TXN1"))
				.body("[0].amount", equalTo(TRANSACTION_AMOUNT.floatValue())).body("[0].currency", equalTo(CURRENCY))
				.body("[0].direction", equalTo(DIRECTION)).body("[0].description", equalTo(DESCRIPTION))
				.body("[1].accountId", equalTo(ACCOUNT_ID_ONE)).body("[1].transactionId", equalTo("TXN2"))
				.body("[1].amount", equalTo(DEBIT_TRANSACTION_AMOUNT.floatValue()))
				.body("[1].currency", equalTo(CURRENCY)).body("[1].direction", equalTo(TransactionDirection.OUT.name()))
				.body("[1].description", equalTo(DESCRIPTION));
	}

	@Test
	public void testGetTransactionsWithInvalidAccountId() {

		performTransactions();

		RestAssured.given().contentType(ContentType.JSON).pathParam("accountId", "CA-081-6653")
				.get(TRANSACTIONS_BY_ACCOUNT_ID_RESOURCE).then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	private void performTransactions() {
		RestAssured.given().contentType(ContentType.JSON).body(createTransactionInputDTO).post(TRANSACTION_RESOURCE)
				.then().statusCode(HttpStatus.SC_OK);
		CreateTransactionInputDTO debitTxnInput = new CreateTransactionInputDTO(ACCOUNT_ID_ONE,
				DEBIT_TRANSACTION_AMOUNT, CURRENCY, TransactionDirection.OUT.name(), DESCRIPTION);
		RestAssured.given().contentType(ContentType.JSON).body(debitTxnInput).post(TRANSACTION_RESOURCE).then()
				.statusCode(HttpStatus.SC_OK);
	}
}
