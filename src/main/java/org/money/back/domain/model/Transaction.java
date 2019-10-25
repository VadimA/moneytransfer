package org.money.back.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final String id;
    private final String originAccountId;
    private final String targetAccountId;
    private final BigDecimal amount;

    public Transaction(String originAccountId, String targetAccountId, BigDecimal amount) {
        this.id = UUID.randomUUID().toString();
        this.originAccountId = originAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getOriginAccountId() {
        return originAccountId;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getOriginAccountId(), that.getOriginAccountId()) &&
                Objects.equals(getTargetAccountId(), that.getTargetAccountId()) &&
                Objects.equals(getAmount(), that.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOriginAccountId(), getTargetAccountId(), getAmount());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", originAccountId='" + originAccountId + '\'' +
                ", targetAccountId='" + targetAccountId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
