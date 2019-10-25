package org.money.back.dto;

import java.math.BigDecimal;

public class TransactionDTO {

    private String transactionId;
    private String originAccountId;
    private String targetAccountId;
    private BigDecimal amount;

    public TransactionDTO() {
    }

    public TransactionDTO(String originAccountId, String targetAccountId, BigDecimal amount) {
        this.originAccountId = originAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public TransactionDTO(String transactionId, String originAccountId, String targetAccountId, BigDecimal amount) {
        this(originAccountId, targetAccountId, amount);
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOriginAccountId() {
        return originAccountId;
    }

    public void setOriginAccountId(String originAccountId) {
        this.originAccountId = originAccountId;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(String targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "originAccountId='" + originAccountId + '\'' +
                ", targetAccountId='" + targetAccountId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
