package com.fintech.cleanarchitecture.infrastructure.adapter;

import com.fintech.cleanarchitecture.domain.model.Transaction;
import com.fintech.cleanarchitecture.domain.port.TransactionRepository;
import com.fintech.cleanarchitecture.infrastructure.repository.TransactionEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TransactionJpaRepository implements TransactionRepository {

    private final TransactionSpringDataRepository springDataRepository;
    private final TransactionMapper mapper;

    public TransactionJpaRepository(TransactionSpringDataRepository springDataRepository,
                                     TransactionMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.id() == null) {
            transaction = new Transaction(
                UUID.randomUUID(),
                transaction.accountId(),
                transaction.amount(),
                transaction.type(),
                transaction.status(),
                transaction.timestamp() != null ? transaction.timestamp() : LocalDateTime.now(),
                transaction.description()
            );
        }
        TransactionEntity entity = mapper.toEntity(transaction);
        TransactionEntity savedEntity = springDataRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return springDataRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Transaction> findByAccountId(String accountId) {
        List<TransactionEntity> entities = springDataRepository.findByAccountId(accountId);
        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByAccountIdAndType(String accountId, String type) {
        List<TransactionEntity> entities = springDataRepository.findByAccountIdAndType(accountId, type);
        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByAccountIdAndStatus(String accountId, String status) {
        List<TransactionEntity> entities = springDataRepository.findByAccountIdAndStatus(accountId, status);
        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }
}