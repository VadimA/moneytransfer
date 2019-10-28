package org.money.back.controller;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.eclipse.jetty.server.Server;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.money.back.config.JettyServer;
import org.money.back.domain.model.Account;
import org.money.back.TestUtil;
import org.money.back.domain.repository.AccountRepository;
import org.money.back.domain.repository.impl.AccountRepositoryImpl;
import org.money.back.dto.AccountDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class AccountControllerTest {

    private final AccountRepository accountRepository = AccountRepositoryImpl.getInstance();
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String BALANCE = "balance";
    private static final String ACCOUNT_PATH = "/accounts/";
    private static final int SERVER_PORT = 9090;
    private static final Server server = JettyServer.getServer(SERVER_PORT);

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
        accountRepository.removeAllAccounts();
    }

    @Test
    void shouldCreateAccount() {
        AccountDTO newAccount = TestUtil.getFakeAccountDTO();
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newAccount)
            .when()
                .post(ACCOUNT_PATH)
            .then()
                .body(NAME, equalTo(newAccount.getName()))
                .body(EMAIL, equalTo(newAccount.getEmail()))
                .body(BALANCE, is(newAccount.getBalance().intValue()))
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    void shouldNotCreateAccountWhenAccountWithCurrentEmailAlreadyExists() {
        AccountDTO newAccount = TestUtil.getFakeAccountDTO();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newAccount)
            .when()
                .post(ACCOUNT_PATH)
            .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newAccount)
            .when()
                .post(ACCOUNT_PATH)
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotCreateAccountWhenEmailIsNull() {
        AccountDTO newAccount = new AccountDTO("TestName", null, null);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newAccount)
            .when()
                .post(ACCOUNT_PATH)
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotCreateAccountWhenEmailIsEmpty() {
        AccountDTO newAccount = new AccountDTO("TestName", "", null);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newAccount)
            .when()
                .post(ACCOUNT_PATH)
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotCreateAccountWhenNameIsNull() {
        AccountDTO newAccount = new AccountDTO(null, "TestEmail", null);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newAccount)
            .when()
                .post(ACCOUNT_PATH)
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotCreateAccountWhenNameIsEmpty() {
        AccountDTO newAccount = new AccountDTO("", "TestEmail", null);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newAccount)
            .when()
                .post(ACCOUNT_PATH)
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldGetAllAccounts() {
        accountRepository.addNewAccount(TestUtil.getFakeAccountWithPassedEmail("test1"));
        accountRepository.addNewAccount(TestUtil.getFakeAccountWithPassedEmail("test2"));
        given()
            .when()
                .get(ACCOUNT_PATH)
            .then()
                .body("", Matchers.hasSize(2))
                .body(containsString("test1"))
                .body(containsString("test2"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void shouldGetAccountsById() {
        Account account = new Account("testId1", "testEmail", "Fake Account", new BigDecimal("5000"));
        accountRepository.addNewAccount(account);

        given()
            .when()
                .get(ACCOUNT_PATH + account.getId())
            .then()
                .body(NAME, equalTo(account.getName()))
                .body(EMAIL, equalTo(account.getEmail()))
                .body(BALANCE, is(account.getBalance().intValue()))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @AfterAll
    public static void stopServer() throws Exception {
        server.stop();
    }
}
