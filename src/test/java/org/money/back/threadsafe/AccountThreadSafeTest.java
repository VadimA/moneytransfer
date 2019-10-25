package org.money.back.threadsafe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.money.back.domain.model.Account;
import org.money.back.service.AccountService;

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

public class AccountThreadSafeTest {

    private final AccountService accountService = AccountService.getInstance();
    private static final Logger logger = LogManager.getLogger(AccountThreadSafeTest.class.getName());

    @BeforeEach
    void cleanup() {
        accountService.deleteAllAccounts();
    }

    @Test
    void addNewAccountTest() throws Exception {
        int threads = 1000;
        ExecutorService service = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean running = new AtomicBoolean();
        AtomicInteger overlaps = new AtomicInteger();
        Collection<Future<Account>> futures = new ArrayList<>(threads);
        for (int t = 0; t < threads; ++t) {
            final Account account = new Account(String.format("Account name #%d", t),
                    String.format("Account email #%d", t), new BigDecimal("500" + t));
            futures.add(
                    service.submit(
                            () -> {
                                latch.await();
                                if (running.get()) {
                                    overlaps.incrementAndGet();
                                }
                                running.set(true);
                                Account account2 = accountService.addNewAccount(account);
                                running.set(false);
                                return account2;
                            }
                    )
            );
        }
        latch.countDown();
        for (Future<Account> f : futures) {
            f.get();
        }
        assertThat(overlaps.get(), greaterThan(0));
        logger.info("Overlap: " + overlaps.get());
        assertThat(accountService.getAllAccounts().size(), equalTo(threads));
    }

    @Test
    void addDuplicateAccountTest() throws Exception {
        int threads = 1000;
        ExecutorService service = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean running = new AtomicBoolean();
        AtomicInteger overlaps = new AtomicInteger();
        Collection<Future<Account>> futures = new ArrayList<>(threads);
        for (int t = 0; t < threads; ++t) {
            final Account account = new Account(String.format("Account name #%d", t),
                    String.format("Account duplicate email", t), new BigDecimal("500" + t));
            futures.add(
                    service.submit(
                            () -> {
                                latch.await();
                                if (running.get()) {
                                    overlaps.incrementAndGet();
                                }
                                running.set(true);
                                Account account2 = accountService.addNewAccount(account);
                                running.set(false);
                                return account2;
                            }
                    )
            );
        }
        latch.countDown();

        for (Future<Account> f : futures) {
            try {
                f.get();
            } catch (Exception ex) {
                Assertions.assertEquals(1, accountService.getAllAccounts().size());
            }
        }
        logger.info("Overlap: " + overlaps.get());
    }
}
