package com.fintech.cleanarchitecture.infrastructure.controller;

import com.fintech.cleanarchitecture.domain.model.Transaction;
import com.fintech.cleanarchitecture.domain.port.TransactionRepository;
import com.fintech.cleanarchitecture.domain.port.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "API para gestión de transacciones financieras")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService, 
                                  TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping
    @Operation(summary = "Crear nueva transacción", description = "Procesa una nueva transacción financiera")
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody Transaction transaction) {
        Transaction processedTransaction = transactionService.processTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(processedTransaction);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transacción por ID", description = "Recupera una transacción específica por su identificador")
    public ResponseEntity<Transaction> getTransactionById(
            @Parameter(description = "UUID de la transacción") @PathVariable UUID id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Listar transacciones por cuenta", description = "Obtiene todas las transacciones asociadas a una cuenta")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(
            @Parameter(description = "ID de la cuenta") @PathVariable String accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/type/{type}")
    @Operation(summary = "Listar transacciones por cuenta y tipo", description = "Filtra transacciones por cuenta y tipo")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountIdAndType(
            @Parameter(description = "ID de la cuenta") @PathVariable String accountId,
            @Parameter(description = "Tipo de transacción") @PathVariable String type) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountIdAndType(accountId, type);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}/pending")
    @Operation(summary = "Listar transacciones pendientes", description = "Obtiene las transacciones pendientes de una cuenta")
    public ResponseEntity<List<Transaction>> getPendingTransactions(
            @Parameter(description = "ID de la cuenta") @PathVariable String accountId) {
        List<Transaction> transactions = transactionService.getPendingTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de transacción", description = "Modifica el estado de una transacción existente")
    public ResponseEntity<Transaction> updateTransactionStatus(
            @Parameter(description = "UUID de la transacción") @PathVariable UUID id,
            @Parameter(description = "Nuevo estado") @RequestParam String status) {
        return transactionService.updateTransactionStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar transacción", description = "Elimina una transacción por su ID")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "UUID de la transacción") @PathVariable UUID id) {
        transactionRepository.findById(id).ifPresent(transaction -> {
            transactionRepository.deleteById(id);
        });
        return ResponseEntity.noContent().build();
    }
}