package org.money.back.mapper;

import org.eclipse.jetty.util.StringUtil;
import org.money.back.domain.model.Account;
import org.money.back.dto.AccountDTO;

import java.util.Objects;

public class AccountMapper {

    public static Account toAccount(AccountDTO accountDTO) {
        if (Objects.isNull(accountDTO) || StringUtil.isEmpty(accountDTO.getEmail())
                || StringUtil.isEmpty(accountDTO.getName())){
            throw new IllegalArgumentException("Account can not be created: " + accountDTO);
        }
        return new Account(accountDTO.getName(), accountDTO.getEmail(), accountDTO.getBalance());
    }

    public static AccountDTO toAccountDTO(Account account) {
        return account != null
                ? new AccountDTO(account.getId(), account.getName(), account.getEmail(), account.getBalance())
                : null;
    }
}
