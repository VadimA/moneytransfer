package org.money.back.service;

import org.money.back.domain.exception.AccountNotFoundException;
import org.money.back.domain.exception.DuplicateAccountException;
import org.money.back.domain.model.Account;
import org.money.back.domain.repository.AccountRepository;
import org.money.back.domain.repository.impl.AccountRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class AccountService {

    private static final AccountService INSTANCE = new AccountService();
    private final AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    private AccountService() {
    }

    public static AccountService getInstance() {
        return INSTANCE;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    public Account getAccountById(String accountId) {
        Account account = accountRepository.getAccountById(accountId);
        if (Objects.isNull(account)) {
            throw new AccountNotFoundException(accountId);
        }
        return accountRepository.getAccountById(accountId);
    }

    public Account addNewAccount(Account account) {
        synchronized (this) {
            Account existsAccount = accountRepository.getAccountByEmail(account.getEmail());
            if (Objects.nonNull(existsAccount)) {
                throw new DuplicateAccountException(account.getEmail());
            }
            return accountRepository.addNewAccount(account);
        }
    }

    public void deleteAllAccounts() {
       accountRepository.removeAllAccounts();
    }
}
