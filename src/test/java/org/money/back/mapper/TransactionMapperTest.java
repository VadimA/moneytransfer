package org.money.back.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.money.back.TestUtil;
import org.money.back.domain.model.Transaction;
import org.money.back.dto.TransactionDTO;

import java.math.BigDecimal;

public class TransactionMapperTest {

    @Test
    void testMapperTransactionToTransactionDTO(){
        Transaction transaction = TestUtil.getFakeTransaction();
        TransactionDTO transactionDTO = TransactionMapper.toTransactionDTO(transaction);
        Assertions.assertEquals(transaction.getId(), transactionDTO.getTransactionId());
        Assertions.assertEquals(transaction.getOriginAccountId(), transactionDTO.getOriginAccountId());
        Assertions.assertEquals(transaction.getTargetAccountId(), transactionDTO.getTargetAccountId());
        Assertions.assertEquals(transaction.getAmount(), transactionDTO.getAmount());
    }

    @Test
    void testMapperTransactionDTOToTransaction(){
        TransactionDTO transactionDTO = new TransactionDTO("originTestId", "targetTestId",
                new BigDecimal("999"));
        Transaction transaction = TransactionMapper.toTransaction(transactionDTO);
        Assertions.assertEquals(transactionDTO.getOriginAccountId(), transaction.getOriginAccountId());
        Assertions.assertEquals(transactionDTO.getTargetAccountId(), transaction.getTargetAccountId());
        Assertions.assertEquals(transactionDTO.getAmount(), transaction.getAmount());
    }
}
