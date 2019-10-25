package org.money.back.threadsafe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.money.back.domain.model.Account;
import org.money.back.domain.model.Transaction;
import org.money.back.service.AccountService;
import org.money.back.service.TransactionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class TransactionThreadSafeTest {

    private static final Logger logger = LogManager.getLogger(AccountThreadSafeTest.class.getName());
    private final TransactionService transactionService = TransactionService.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    @Test
    void createTransactionsBetweenTwoAccounts() throws Exception {
        Account account1 = new Account("OriginAccountId","originTestId", "test1", new BigDecimal("5000000"));
        Account account2 = new Account("TargetAccountId","targetTestId", "test2", new BigDecimal("5000000"));
        accountService.addNewAccount(account1);
        accountService.addNewAccount(account2);
        int threads = 1000;
        ExecutorService service =
                Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean running = new AtomicBoolean();
        AtomicInteger overlaps = new AtomicInteger();
        Collection<Future<Transaction>> futures =
                new ArrayList<>(threads);
        for (int t = 0; t < threads; ++t) {
            final Transaction transaction1 = new Transaction("OriginAccountId", "TargetAccountId", new BigDecimal(t));
            final Transaction transaction2 = new Transaction("TargetAccountId", "OriginAccountId", new BigDecimal(t));

            futures.add(
                    service.submit(
                            () -> {
                                latch.await();
                                if (running.get()) {
                                    overlaps.incrementAndGet();
                                }
                                running.set(true);
                                Transaction transaction3 = transactionService.createNewTransaction(transaction1);
                                transactionService.createNewTransaction(transaction2);
                                running.set(false);
                                return transaction3;
                            }
                    )
            );
        }
        latch.countDown();
        for (Future<Transaction> f : futures) {
            f.get();
        }
        assertThat(overlaps.get(), greaterThan(0));
        logger.info("Overlap: " + overlaps.get());
        assertThat(transactionService.getAllTransactions().size(), equalTo(threads * 2));
        assertThat(account1.getBalance(), equalTo(account2.getBalance()));
    }
}
