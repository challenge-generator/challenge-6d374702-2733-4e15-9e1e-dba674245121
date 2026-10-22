package com.fintech.cleanarchitecture.application.usecase;

import com.fintech.cleanarchitecture.domain.model.Transaction;
import com.fintech.cleanarchitecture.domain.port.TransactionRepository;
import com.fintech.cleanarchitecture.domain.port.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private ProcessTransactionUseCase processTransactionUseCase;

    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        sampleTransaction = new Transaction(
            UUID.randomUUID(),
            "ACC-001",
            "DEPOSIT",
            new BigDecimal("1000.00"),
            "USD",
            "COMPLETED",
            null,
            null
        );
    }

    @Test
    @Disabled("Superficie de práctica - implementar validación de transacción entrante")
    void testProcessTransaction_ValidInput_ReturnsProcessedTransaction() {
        when(transactionService.processTransaction(any(Transaction.class)))
            .thenReturn(sampleTransaction);

        Transaction result = processTransactionUseCase.execute(sampleTransaction);

        assertNotNull(result);
        verify(transactionService, times(1)).processTransaction(any(Transaction.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @Disabled("Superficie de práctica - manejar caso de transacción nula")
    void testProcessTransaction_NullInput_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            processTransactionUseCase.execute(null);
        });
    }

    @Test
    @Disabled("Superficie de práctica - probar lógica de retry en caso de fallo")
    void testProcessTransaction_RetryOnFailure_RetriesThreeTimes() {
        when(transactionService.processTransaction(any(Transaction.class)))
            .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> {
            processTransactionUseCase.execute(sampleTransaction);
        });

        verify(transactionService, times(3)).processTransaction(any(Transaction.class));
    }

    @Test
    @Disabled("Superficie de práctica - verificar que se registre en métricas")
    void testProcessTransaction_Success_RecordsMetrics() {
        Transaction completedTransaction = new Transaction(
            sampleTransaction.id(),
            sampleTransaction.accountId(),
            sampleTransaction.type(),
            sampleTransaction.amount(),
            sampleTransaction.currency(),
            "COMPLETED",
            null,
            null
        );

        when(transactionService.processTransaction(any(Transaction.class)))
            .thenReturn(completedTransaction);

        Transaction result = processTransactionUseCase.execute(sampleTransaction);

        assertEquals("COMPLETED", result.status());
    }

    @Test
    @Disabled("Superficie de práctica - probar actualización de estado de cuenta")
    void testProcessTransaction_UpdatesAccountBalance() {
        when(transactionService.processTransaction(any(Transaction.class)))
            .thenReturn(sampleTransaction);

        processTransactionUseCase.execute(sampleTransaction);

        verify(transactionService, times(1)).processTransaction(any(Transaction.class));
    }
}