package com.fintech.cleanarchitecture.domain.port;

import com.fintech.cleanarchitecture.domain.model.Transaction;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    Transaction processTransaction(Transaction transaction);

    Transaction getTransactionById(UUID id);

    List<Transaction> getTransactionsByAccountId(String accountId);

    List<Transaction> getTransactionsByAccountIdAndType(String accountId, String type);

    List<Transaction> getPendingTransactions(String accountId);

    Transaction updateTransactionStatus(UUID id, String newStatus);
}