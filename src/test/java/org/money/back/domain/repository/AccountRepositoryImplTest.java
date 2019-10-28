package org.money.back.domain.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.money.back.domain.model.Account;
import org.money.back.TestUtil;
import org.money.back.domain.repository.impl.AccountRepositoryImpl;

public class AccountRepositoryImplTest {

    private static final int NUMBER_OF_ACCOUNTS = 5;
    private static final String TEST_EMAIL = "testEmail";
    private final AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    @BeforeEach
    void cleanUp() {
        accountRepository.removeAllAccounts();
    }

    @Test
    void createNewAccountTest() {
        Account account = TestUtil.getFakeAccount();
        accountRepository.addNewAccount(account);

        Assertions.assertEquals(1, accountRepository.getAllAccounts().size());
        Assertions.assertEquals(account, accountRepository.getAccountById(account.getId()));
    }

    @Test
    void getAllAccountsTest() {
        for(int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
            accountRepository.addNewAccount(TestUtil.getFakeAccountWithPassedEmail(TEST_EMAIL + i));
        }

        Assertions.assertEquals(NUMBER_OF_ACCOUNTS, accountRepository.getAllAccounts().size());
        Assertions.assertNotNull(accountRepository.getAllAccounts().get(NUMBER_OF_ACCOUNTS - 1));
    }

    @Test
    void removeAllAccountsTest() {
        for(int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
            accountRepository.addNewAccount(TestUtil.getFakeAccountWithPassedEmail(TEST_EMAIL + i));
        }

        Assertions.assertEquals(NUMBER_OF_ACCOUNTS, accountRepository.getAllAccounts().size());
        accountRepository.removeAllAccounts();

        Assertions.assertEquals(0, accountRepository.getAllAccounts().size());
    }

    @Test
    void getAccountById() {
        Account account = TestUtil.getFakeAccount();
        accountRepository.addNewAccount(account);

        Assertions.assertEquals(1, accountRepository.getAllAccounts().size());
        Assertions.assertSame(account, accountRepository.getAccountById(account.getId()));
    }
}
