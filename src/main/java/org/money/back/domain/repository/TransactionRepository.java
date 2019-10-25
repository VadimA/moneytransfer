package org.money.back.domain.repository;

import org.money.back.domain.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(String transactionId);

    Transaction createNewTransaction(Transaction transaction);

    void removeAllTransactions();
}
