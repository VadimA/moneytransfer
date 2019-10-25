package org.money.back.domain.repository;

import org.money.back.domain.model.Account;

import java.util.List;

public interface AccountRepository {

    Account addNewAccount(Account account);

    Account getAccountById(String accountId);

    Account getAccountByEmail(String email);

    List<Account> getAllAccounts();

    void removeAllAccounts();
}
