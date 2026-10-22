package com.fintech.cleanarchitecture.domain.port;

import com.fintech.cleanarchitecture.domain.model.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(UUID id);

    List<Transaction> findByAccountId(String accountId);

    List<Transaction> findByAccountIdAndType(String accountId, String type);

    List<Transaction> findByAccountIdAndStatus(String accountId, String status);

    void deleteById(UUID id);
}