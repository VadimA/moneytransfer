package org.money.back.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.money.back.TestUtil;
import org.money.back.domain.exception.TransactionNotFoundException;
import org.money.back.domain.model.Account;
import org.money.back.domain.model.Transaction;

import java.math.BigDecimal;

import static org.money.back.TestUtil.TRANSACTION_AMOUNT;

public class TransactionServiceTest {

    private final TransactionService transactionService = TransactionService.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    @BeforeEach
    void cleanup() {
        transactionService.deleteAllTransactions();
        accountService.deleteAllAccounts();
        Account account1 = new Account("originTestId","originTestId", "test1", new BigDecimal("5000"));
        Account account2 = new Account("targetTestId","targetTestId", "test2", new BigDecimal("5000"));
        accountService.addNewAccount(account1);
        accountService.addNewAccount(account2);
    }

    @Test
    void addNewTransactionsTest() {
        Transaction newTransaction = TestUtil.getFakeTransaction();
        transactionService.createNewTransaction(newTransaction);

        Assertions.assertEquals(1, transactionService.getAllTransactions().size());
        Assertions.assertSame(newTransaction, transactionService.getTransactionById(newTransaction.getId()));
    }

    @Test
    void addTransactionFromAccountToTheSameAccountTest() {
        Transaction newTransaction = new Transaction("originTestId", "originTestId", TRANSACTION_AMOUNT);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> transactionService.createNewTransaction(newTransaction));
    }

    @Test
    void getAllTransactionsTest() {
        Transaction transaction = TestUtil.getFakeTransaction();
        Transaction transaction2 = TestUtil.getFakeTransaction();
        transactionService.createNewTransaction(transaction);
        transactionService.createNewTransaction(transaction2);

        Assertions.assertEquals(2, transactionService.getAllTransactions().size());
        Assertions.assertTrue(transactionService.getAllTransactions().contains(transaction));
        Assertions.assertTrue(transactionService.getAllTransactions().contains(transaction2));
    }

    @Test
    void getTransactionsByIdTest() {
        Transaction transaction = TestUtil.getFakeTransaction();
        transactionService.createNewTransaction(transaction);

        Assertions.assertEquals(transaction.getId(), transactionService.getTransactionById(transaction.getId()).getId());
        Assertions.assertSame(transaction, transactionService.getTransactionById(transaction.getId()));
    }

    @Test
    void getNotExistTransactionByIdTest() {
        Transaction transaction = TestUtil.getFakeTransaction();
        transactionService.createNewTransaction(transaction);

        Assertions.assertThrows(TransactionNotFoundException.class,
                () -> transactionService.getTransactionById("NotExistsId"));
    }
}
