package org.money.back.controller;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.eclipse.jetty.server.Server;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.money.back.TestUtil;
import org.money.back.config.JettyServer;
import org.money.back.domain.model.Account;
import org.money.back.domain.model.Transaction;
import org.money.back.domain.repository.AccountRepository;
import org.money.back.domain.repository.TransactionRepository;
import org.money.back.domain.repository.impl.AccountRepositoryImpl;
import org.money.back.domain.repository.impl.TransactionRepositoryImpl;
import org.money.back.dto.TransactionDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class TransactionControllerTest {

    private static final String ORIGIN_ACCOUNT_ID = "originAccountId";
    private static final String TARGET_ACCOUNT_ID = "targetAccountId";
    private static final String AMOUNT = "amount";
    private static final String TRANSACTION_PATH = "/transactions/";
    private static final BigDecimal WITDRAW_AMOUNT_MORE_THAN_BALANCE = new BigDecimal("10500");
    private static final int SERVER_PORT = 9090;
    private static final Server server = JettyServer.getServer(SERVER_PORT);
    private final AccountRepository accountRepository = AccountRepositoryImpl.getInstance();
    private final TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();

    @BeforeAll
    static void setup() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
        startServer();
    }

    static void startServer() throws Exception {
        server.start();
    }

    @BeforeEach
    void cleanUp() {
        transactionRepository.removeAllTransactions();
    }

    @Test
    void shouldGetAllTransactions() {
        transactionRepository.createNewTransaction(TestUtil.getFakeTransaction());
        transactionRepository.createNewTransaction(TestUtil.getFakeTransaction());
        given()
            .when()
                .get(TRANSACTION_PATH)
            .then()
                .body("", Matchers.hasSize(2))
                .body(containsString("originTestId"))
                .body(containsString("targetTestId"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void shouldGetTransactionById() {
        Transaction transaction = transactionRepository.createNewTransaction(TestUtil.getFakeTransaction());
        given()
            .when()
                .get(TRANSACTION_PATH + transaction.getId())
            .then()
                .body(ORIGIN_ACCOUNT_ID, equalTo(transaction.getOriginAccountId()))
                .body(TARGET_ACCOUNT_ID, equalTo(transaction.getTargetAccountId()))
                .body(AMOUNT, is(transaction.getAmount().intValue()))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void shouldNotGetTransactionByNonExistId() {
        transactionRepository.createNewTransaction(TestUtil.getFakeTransaction());
        given()
                .when()
                .get(TRANSACTION_PATH + "nonExistsId")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldPerformTransaction() {
        TransactionDTO newTransaction = prepareTestTransactionDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newTransaction)
            .when()
                .post(TRANSACTION_PATH)
            .then()
                .body(ORIGIN_ACCOUNT_ID, equalTo(newTransaction.getOriginAccountId()))
                .body(TARGET_ACCOUNT_ID, equalTo(newTransaction.getTargetAccountId()))
                .body(AMOUNT, is(newTransaction.getAmount().intValue()))
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    void shouldNotPerformTransactionToInvalidAccount() {
        Account account = new Account("originTestId", "testEmail", "Fake Account", new BigDecimal("5000"));
        accountRepository.addNewAccount(account);
        TransactionDTO newTransaction = new TransactionDTO(account.getId(), "invalidAccountId", new BigDecimal("500"));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newTransaction)
            .when()
                .post(TRANSACTION_PATH)
            .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldNotPerformTransactionFromInvalidAccount() {
        Account account = new Account("originTestId", "testEmail", "Fake Account", new BigDecimal("5000"));
        accountRepository.addNewAccount(account);
        TransactionDTO newTransaction = new TransactionDTO("invalidAccountId", account.getId(), new BigDecimal("500"));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newTransaction)
            .when()
                .post(TRANSACTION_PATH)
            .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldNotPerformTransactionWithInvalidAmount() {
        TransactionDTO newTransaction = prepareTestTransactionDTO();
        newTransaction.setAmount(new BigDecimal("-500"));
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newTransaction)
            .when()
                .post(TRANSACTION_PATH)
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotPerformTransactionWithNullAmount() {
        TransactionDTO newTransaction = prepareTestTransactionDTO();
        newTransaction.setAmount(null);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newTransaction)
            .when()
                .post(TRANSACTION_PATH)
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotPerformTransactionToItself() {
        Account account1 = new Account("originTestId", "testEmail", "Fake Account", new BigDecimal("5000"));
        accountRepository.addNewAccount(account1);
        TransactionDTO newTransaction = new TransactionDTO(account1.getId(), account1.getId(), new BigDecimal("500"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newTransaction)
                .when()
                .post(TRANSACTION_PATH)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotPerformTransactionForInsufficientBalance() {
        TransactionDTO newTransaction = prepareTestTransactionDTO();
        newTransaction.setAmount(WITDRAW_AMOUNT_MORE_THAN_BALANCE);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newTransaction)
                .when()
                .post(TRANSACTION_PATH)
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }

    private TransactionDTO prepareTestTransactionDTO() {
        Account account1 = new Account("originTestId", "testEmail", "Fake Account", new BigDecimal("5000"));
        Account account2 = new Account("targetTestId", "testEmail", "Fake Account", new BigDecimal("5000"));
        accountRepository.addNewAccount(account1);
        accountRepository.addNewAccount(account2);
        return new TransactionDTO(account1.getId(), account2.getId(), new BigDecimal("500"));
    }

    @AfterAll
    static void stopServer() throws Exception {
        server.stop();
    }
}
