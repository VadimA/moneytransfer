package org.money.back.domain.repository.impl;

import org.money.back.domain.model.Account;
import org.money.back.domain.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepositoryImpl implements AccountRepository {

    private static final AccountRepository INSTANCE = new AccountRepositoryImpl();
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    private AccountRepositoryImpl() {
    }

    public static AccountRepository getInstance(){
        return INSTANCE;
    }

    @Override
    public Account addNewAccount(Account account) {
        accounts.putIfAbsent(account.getId(), account);
        return getAccountById(account.getId());
    }

    @Override
    public Account getAccountById(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accounts.values().stream()
                .filter(acc -> acc.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void removeAllAccounts(){
        accounts.clear();
    }
}
