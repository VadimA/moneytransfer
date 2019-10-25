package org.money.back.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.money.back.TestUtil;
import org.money.back.domain.exception.InsufficientBalanceException;

import java.math.BigDecimal;

public class AccountTest {

    private static final BigDecimal WITDRAW_AMOUNT = new BigDecimal("5500");
    private static final BigDecimal WITDRAW_AMOUNT_MORE_THAN_BALANCE = new BigDecimal("10500");
    private static final BigDecimal WITDRAW_NEGATIVE_AMOUNT = new BigDecimal("-550");
    private static final BigDecimal BALANCE_AFTER_WITDRAW = new BigDecimal("4500");
    private static final BigDecimal BALANCE_AFTER_DEPOSIT = new BigDecimal("15500");

    @Test
    void withdrawHappyPath(){
        Account account = TestUtil.getFakeAccount();
        account.withdraw(WITDRAW_AMOUNT);
        Assertions.assertEquals(BALANCE_AFTER_WITDRAW, account.getBalance());
    }

    @Test
    void withdrawNullAmount(){
        Account account = TestUtil.getFakeAccount();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(null));
    }

    @Test
    void withdrawEmptyAmount(){
        Account account = TestUtil.getFakeAccount();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(new BigDecimal("")));
    }

    @Test
    void withdrawNegative(){
        Account account = TestUtil.getFakeAccount();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(WITDRAW_NEGATIVE_AMOUNT));
    }

    @Test
    void depositHappyPath(){
        Account account = TestUtil.getFakeAccount();
        account.deposit(WITDRAW_AMOUNT);
        Assertions.assertEquals(BALANCE_AFTER_DEPOSIT, account.getBalance());
    }

    @Test
    void depositNullAmount(){
        Account account = TestUtil.getFakeAccount();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> account.deposit(null));
    }

    @Test
    void depositEmptyAmount(){
        Account account = TestUtil.getFakeAccount();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> account.deposit(new BigDecimal("")));
    }

    @Test
    void depositNegative(){
        Account account = TestUtil.getFakeAccount();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> account.deposit(WITDRAW_NEGATIVE_AMOUNT));
    }

    @Test
    void insufficientBalanceTest(){
        Account account = TestUtil.getFakeAccount();
        Assertions.assertThrows(InsufficientBalanceException.class,
                () -> account.withdraw(WITDRAW_AMOUNT_MORE_THAN_BALANCE));
    }
}
