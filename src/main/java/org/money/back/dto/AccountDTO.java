package org.money.back.dto;

import java.math.BigDecimal;

public class AccountDTO {

    private String id;

    private String name;

    private String email;

    private BigDecimal balance;

    public AccountDTO(){}

    public AccountDTO(String name, String email, BigDecimal balance) {
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public AccountDTO(String id, String name, String email, BigDecimal balance) {
        this(name, email, balance);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }
}
