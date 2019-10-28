package org.money.back.domain.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.money.back.TestUtil;
import org.money.back.domain.model.Account;
import org.money.back.domain.model.Transaction;
import org.money.back.domain.repository.impl.AccountRepositoryImpl;
import org.money.back.domain.repository.impl.TransactionRepositoryImpl;

import java.math.BigDecimal;

public class TransactionRepositoryTest {

    private static final int NUMBER_OF_TRANSACTIONS = 5;

    private final TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();
    private final AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    @BeforeEach
    void cleanUp() {
        transactionRepository.removeAllTransactions();
        accountRepository.removeAllAccounts();
        Account account1 = new Account("originTestId","originTestId", "test1", new BigDecimal("5000"));
        Account account2 = new Account("targetTestId","targetTestId", "test2", new BigDecimal("5000"));
        accountRepository.addNewAccount(account1);
        accountRepository.addNewAccount(account2);
    }

    @Test
    void createNewTransactionTest() {
        Transaction transaction = TestUtil.getFakeTransaction();
        transactionRepository.createNewTransaction(transaction);

        Assertions.assertEquals(1, transactionRepository.getAllTransactions().size());
        Assertions.assertEquals(transaction, transactionRepository.getTransactionById(transaction.getId()));
    }

    @Test
    void getAllTransactionsTest() {
        for(int i = 0; i < NUMBER_OF_TRANSACTIONS; i++) {
            transactionRepository.createNewTransaction(TestUtil.getFakeTransaction());
        }

        Assertions.assertEquals(NUMBER_OF_TRANSACTIONS, transactionRepository.getAllTransactions().size());
        Assertions.assertNotNull(transactionRepository.getAllTransactions().get(NUMBER_OF_TRANSACTIONS - 1));
    }

    @Test
    void removeAllTransactionsTest() {
        for(int i = 0; i < NUMBER_OF_TRANSACTIONS; i++) {
            transactionRepository.createNewTransaction(TestUtil.getFakeTransaction());
        }

        Assertions.assertEquals(NUMBER_OF_TRANSACTIONS, transactionRepository.getAllTransactions().size());
        transactionRepository.removeAllTransactions();

        Assertions.assertEquals(0, transactionRepository.getAllTransactions().size());
    }

    @Test
    void getTransactionById() {
        Transaction transaction = TestUtil.getFakeTransaction();
        transactionRepository.createNewTransaction(transaction);

        Assertions.assertSame(transaction, transactionRepository.getTransactionById(transaction.getId()));
    }
}
