package org.money.back.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.money.back.TestUtil;
import org.money.back.domain.exception.AccountNotFoundException;
import org.money.back.domain.exception.DuplicateAccountException;
import org.money.back.domain.model.Account;

public class AccountServiceTest {

    private static final String TEST_EMAIL = "testEmail";
    private final AccountService accountService = AccountService.getInstance();

    @BeforeEach
    void cleanup() {
        accountService.deleteAllAccounts();
    }

    @Test
    void addNewAccountsTest() {
        Account account = TestUtil.getFakeAccount();
        accountService.addNewAccount(account);

        Assertions.assertEquals(1, accountService.getAllAccounts().size());
        Assertions.assertSame(account, accountService.getAccountById(account.getId()));
    }

    @Test
    void addDuplicateAccountsTest() {
        Account account = TestUtil.getFakeAccount();
        accountService.addNewAccount(account);

        Account duplicateAccount = TestUtil.getFakeAccount();

        Assertions.assertThrows(DuplicateAccountException.class,
                () -> accountService.addNewAccount(duplicateAccount));
        Assertions.assertEquals(1, accountService.getAllAccounts().size());
    }

    @Test
    void getAllAccountsTest() {
        Account account1 = TestUtil.getFakeAccountWithPassedEmail(TEST_EMAIL + 1);
        Account account2 = TestUtil.getFakeAccountWithPassedEmail(TEST_EMAIL + 2);
        accountService.addNewAccount(account1);
        accountService.addNewAccount(account2);

        Assertions.assertEquals(2, accountService.getAllAccounts().size());
        Assertions.assertTrue(accountService.getAllAccounts().contains(account1));
        Assertions.assertTrue(accountService.getAllAccounts().contains(account2));
    }

    @Test
    void getAccountsByIdTest() {
        Account account = TestUtil.getFakeAccount();
        accountService.addNewAccount(account);

        Assertions.assertEquals(account.getId(), accountService.getAccountById(account.getId()).getId());
        Assertions.assertSame(account, accountService.getAccountById(account.getId()));
    }

    @Test
    void getNotExistAccountByIdTest() {
        Account account = TestUtil.getFakeAccountWithPassedEmail("existsId");
        accountService.addNewAccount(account);

        Assertions.assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountById("NotExistsId"));
    }
}
