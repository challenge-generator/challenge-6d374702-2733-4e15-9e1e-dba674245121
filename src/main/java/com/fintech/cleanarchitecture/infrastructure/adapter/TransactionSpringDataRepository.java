package com.fintech.cleanarchitecture.infrastructure.adapter;

import com.fintech.cleanarchitecture.infrastructure.repository.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionSpringDataRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByAccountId(String accountId);
    List<TransactionEntity> findByAccountIdAndType(String accountId, String type);
    List<TransactionEntity> findByAccountIdAndStatus(String accountId, String status);
}