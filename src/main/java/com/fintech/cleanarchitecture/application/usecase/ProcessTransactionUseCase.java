package com.fintech.cleanarchitecture.application.usecase;

import com.fintech.cleanarchitecture.domain.model.Transaction;
import com.fintech.cleanarchitecture.domain.port.TransactionRepository;
import com.fintech.cleanarchitecture.domain.port.TransactionService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ProcessTransactionUseCase implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessTransactionUseCase.class);
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_FAILED = "FAILED";

    private final TransactionRepository transactionRepository;
    private final Validator validator;

    public ProcessTransactionUseCase(TransactionRepository transactionRepository,
                                      Validator validator) {
        this.transactionRepository = transactionRepository;
        this.validator = validator;
    }

    @Override
    public Transaction processTransaction(Transaction transaction) {
        logger.info("Procesando transacción para cuenta: {}", transaction.accountId());
        
        validarTransaccion(transaction);
        
        Transaction transaccionParaProcesar = new Transaction(
            transaction.id() != null ? transaction.id() : UUID.randomUUID(),
            transaction.accountId(),
            transaction.amount(),
            transaction.type(),
            STATUS_PENDING,
            transaction.timestamp() != null ? transaction.timestamp() : LocalDateTime.now(),
            transaction.description()
        );
        
        Transaction transaccionGuardada = transactionRepository.save(transaccionParaProcesar);
        
        boolean procesamientoExitoso = ejecutarProcesamiento(transaccionGuardada);
        
        String nuevoEstado = procesamientoExitoso ? STATUS_COMPLETED : STATUS_FAILED;
        Transaction transaccionActualizada = transaccionGuardada.withStatus(nuevoEstado);
        
        logger.info("Transacción {} procesada con estado: {}", transaccionActualizada.id(), nuevoEstado);
        
        return transactionRepository.save(transaccionActualizada);
    }

    private void validarTransaccion(Transaction transaction) {
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        if (!violations.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Errores de validación: ");
            violations.forEach(v -> mensaje.append(v.getMessage()).append("; "));
            logger.error("Validación fallida: {}", mensaje);
            throw new ValidacionTransaccionException(mensaje.toString());
        }
        
        if (transaction.amount() == null || transaction.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionTransaccionException("El monto debe ser mayor que cero");
        }
        
        if (transaction.accountId() == null || transaction.accountId().isBlank()) {
            throw new ValidacionTransaccionException("El ID de cuenta no puede estar vacío");
        }
    }

    private boolean ejecutarProcesamiento(Transaction transaction) {
        try {
            String tipo = transaction.type();
            if ("DEPOSIT".equalsIgnoreCase(tipo)) {
                return procesarDeposito(transaction);
            } else if ("WITHDRAWAL".equalsIgnoreCase(tipo)) {
                return procesarRetiro(transaction);
            } else if ("TRANSFER".equalsIgnoreCase(tipo)) {
                return procesarTransferencia(transaction);
            } else {
                logger.warn("Tipo de transacción desconocido: {}", tipo);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al procesar transacción {}: {}", transaction.id(), e.getMessage());
            return false;
        }
    }

    private boolean procesarDeposito(Transaction transaction) {
        logger.debug("Procesando depósito por monto: {}", transaction.amount());
        return true;
    }

    private boolean procesarRetiro(Transaction transaction) {
        logger.debug("Procesando retiro por monto: {}", transaction.amount());
        return true;
    }

    private boolean procesarTransferencia(Transaction transaction) {
        logger.debug("Procesando transferencia por monto: {}", transaction.amount());
        return true;
    }

    @Override
    public Transaction getTransactionById(UUID id) {
        logger.debug("Obteniendo transacción por ID: {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransaccionNoEncontradaException("Transacción no encontrada: " + id));
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(String accountId) {
        logger.debug("Obteniendo transacciones para cuenta: {}", accountId);
        return transactionRepository.findByAccountId(accountId);
    }

    @Override
    public List<Transaction> getTransactionsByAccountIdAndType(String accountId, String type) {
        logger.debug("Obteniendo transacciones tipo {} para cuenta: {}", type, accountId);
        return transactionRepository.findByAccountIdAndType(accountId, type);
    }

    @Override
    public List<Transaction> getPendingTransactions(String accountId) {
        logger.debug("Obteniendo transacciones pendientes para cuenta: {}", accountId);
        return transactionRepository.findByAccountIdAndStatus(accountId, STATUS_PENDING);
    }

    @Override
    public Transaction updateTransactionStatus(UUID id, String newStatus) {
        logger.info("Actualizando estado de transacción {} a {}", id, newStatus);
        Transaction transaction = getTransactionById(id);
        Transaction actualizad = transaction.withStatus(newStatus);
        return transactionRepository.save(actualizad);
    }
    
    public static class ValidacionTransaccionException extends RuntimeException {
        public ValidacionTransaccionException(String message) {
            super(message);
        }
    }
    
    public static class TransaccionNoEncontradaException extends RuntimeException {
        public TransaccionNoEncontradaException(String message) {
            super(message);
        }
    }
}