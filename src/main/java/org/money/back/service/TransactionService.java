package org.money.back.service;

import org.money.back.domain.exception.AccountNotFoundException;
import org.money.back.domain.exception.TransactionNotFoundException;
import org.money.back.domain.model.Account;
import org.money.back.domain.model.Transaction;
import org.money.back.domain.repository.AccountRepository;
import org.money.back.domain.repository.TransactionRepository;
import org.money.back.domain.repository.impl.AccountRepositoryImpl;
import org.money.back.domain.repository.impl.TransactionRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class TransactionService {

    private static final TransactionService INSTANCE = new TransactionService();
    private final TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();
    private final AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    private TransactionService() {
    }

    public static TransactionService getInstance() {
        return INSTANCE;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }

    public Transaction getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.getTransactionById(transactionId);
        if (Objects.isNull(transaction)) {
            throw new TransactionNotFoundException(transactionId);
        }
        return transaction;
    }

    //TODO: refactor by performance issue
    public synchronized Transaction createNewTransaction(Transaction transaction) {
        Account originAccount = accountRepository.getAccountById(transaction.getOriginAccountId());
        Account targetAccount = accountRepository.getAccountById(transaction.getTargetAccountId());
        if (originAccount == null || targetAccount == null) {
            throw new AccountNotFoundException(originAccount == null
                    ? transaction.getOriginAccountId()
                    : transaction.getTargetAccountId());
        }

        originAccount.withdraw(transaction.getAmount());
        targetAccount.deposit(transaction.getAmount());
        return transactionRepository.createNewTransaction(transaction);
    }

    void deleteAllTransactions() {
        transactionRepository.removeAllTransactions();
    }
}
