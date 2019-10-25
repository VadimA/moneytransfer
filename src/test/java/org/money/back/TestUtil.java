package org.money.back;

import org.money.back.domain.model.Account;
import org.money.back.domain.model.Transaction;
import org.money.back.dto.AccountDTO;

import java.math.BigDecimal;

public class TestUtil {

    public static final BigDecimal INITIAL_BALANCE = new BigDecimal("10000");
    public static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal("500");

    public static Account getFakeAccountWithPassedEmail(String testEmail){
        return new Account("Fake Account", testEmail, INITIAL_BALANCE);
    }

    public static Account getFakeAccount(){
        return new Account("Fake Account", "Fake Email", INITIAL_BALANCE);
    }

    public static AccountDTO getFakeAccountDTO(){
        return new AccountDTO("Fake Account", "Fake Email", INITIAL_BALANCE);
    }

    public static Transaction getFakeTransaction(){
        return new Transaction("originTestId", "targetTestId", TRANSACTION_AMOUNT);
    }
}
