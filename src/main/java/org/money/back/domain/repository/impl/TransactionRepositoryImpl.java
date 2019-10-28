package org.money.back.domain.repository.impl;

import org.money.back.domain.model.Transaction;
import org.money.back.domain.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionRepositoryImpl implements TransactionRepository {

    private static final TransactionRepository INSTANCE = new TransactionRepositoryImpl();
    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    private TransactionRepositoryImpl() {
    }

    public static TransactionRepository getInstance(){
        return INSTANCE;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        return transactions.get(transactionId);
    }

    @Override
    public Transaction createNewTransaction(Transaction transaction) {
        transactions.putIfAbsent(transaction.getId(), transaction);
        return getTransactionById(transaction.getId());
    }

    @Override
    public void removeAllTransactions() {
        transactions.clear();
    }
}
