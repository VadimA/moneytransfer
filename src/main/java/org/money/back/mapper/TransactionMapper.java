package org.money.back.mapper;

import org.eclipse.jetty.util.StringUtil;
import org.money.back.domain.model.Transaction;
import org.money.back.dto.TransactionDTO;

import java.util.Objects;

public class TransactionMapper {

    public static Transaction toTransaction(TransactionDTO transactionDTO) {
        if (Objects.isNull(transactionDTO) || StringUtil.isEmpty(transactionDTO.getOriginAccountId())
                || StringUtil.isEmpty(transactionDTO.getTargetAccountId())
                || transactionDTO.getAmount() == null ){
            throw new IllegalArgumentException("Transaction can not be created: " + transactionDTO);
        }
        return new Transaction(transactionDTO.getOriginAccountId(), transactionDTO.getTargetAccountId(),
                transactionDTO.getAmount());
    }

    public static TransactionDTO toTransactionDTO(Transaction transaction) {
        return transaction != null
                ? new TransactionDTO(transaction.getId(), transaction.getOriginAccountId(),
                transaction.getTargetAccountId(), transaction.getAmount())
                : null;
    }
}
