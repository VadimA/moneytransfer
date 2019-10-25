package org.money.back.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.money.back.domain.model.Account;
import org.money.back.TestUtil;
import org.money.back.dto.AccountDTO;

import java.math.BigDecimal;

public class AccountMapperTest {

    @Test
    void testMapperAccountToAccountDTO(){
        Account account = TestUtil.getFakeAccount();
        AccountDTO accountDTO = AccountMapper.toAccountDTO(account);
        Assertions.assertEquals(account.getId(), accountDTO.getId());
        Assertions.assertEquals(account.getName(), accountDTO.getName());
        Assertions.assertEquals(accountDTO.getEmail(), account.getEmail());
        Assertions.assertEquals(account.getBalance(), accountDTO.getBalance());
    }

    @Test
    void testMapperAccountDTOToAccount(){
        AccountDTO accountDTO = new AccountDTO("testName", "testEmail", new BigDecimal("999"));
        Account account = AccountMapper.toAccount(accountDTO);
        Assertions.assertEquals(accountDTO.getName(), account.getName());
        Assertions.assertEquals(accountDTO.getEmail(), account.getEmail());
        Assertions.assertEquals(accountDTO.getBalance(), account.getBalance());
    }
}
