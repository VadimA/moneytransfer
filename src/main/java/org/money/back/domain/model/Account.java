package org.money.back.domain.model;

import org.money.back.domain.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private final String id;

    private final String name;

    private final String email;

    private BigDecimal balance;

    public Account(String name, String email){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.balance = BigDecimal.ZERO;
    }

    public Account(String name, String email, BigDecimal balance){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
    }

    public Account(String id, String email, String name, BigDecimal balance){
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(getId());
        }
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount);
    }

    private void validateAmount(BigDecimal amount){
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(getId(), account.getId()) &&
                Objects.equals(getName(), account.getName()) &&
                Objects.equals(getEmail(), account.getEmail()) &&
                Objects.equals(getBalance(), account.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getBalance());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }
}
