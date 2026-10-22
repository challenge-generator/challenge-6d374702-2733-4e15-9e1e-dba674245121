package com.fintech.cleanarchitecture.infrastructure.adapter;

import com.fintech.cleanarchitecture.domain.model.Transaction;
import com.fintech.cleanarchitecture.infrastructure.repository.TransactionEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Transaction(
            entity.getId(),
            entity.getAccountId(),
            entity.getAmount(),
            entity.getType(),
            entity.getStatus(),
            entity.getTimestamp(),
            entity.getDescription()
        );
    }

    public TransactionEntity toEntity(Transaction domain) {
        if (domain == null) {
            return null;
        }
        TransactionEntity entity = new TransactionEntity();
        if (domain.id() != null) {
            entity.setId(domain.id());
        }
        entity.setAccountId(domain.accountId());
        entity.setAmount(domain.amount());
        entity.setType(domain.type());
        entity.setStatus(domain.status());
        entity.setTimestamp(domain.timestamp() != null ? domain.timestamp() : LocalDateTime.now());
        entity.setDescription(domain.description());
        return entity;
    }
}