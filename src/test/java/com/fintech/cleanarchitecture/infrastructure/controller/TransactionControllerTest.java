package com.fintech.cleanarchitecture.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.cleanarchitecture.domain.model.Transaction;
import com.fintech.cleanarchitecture.domain.port.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private Transaction sampleTransaction;
    private String transactionJson;

    @BeforeEach
    void setUp() throws Exception {
        sampleTransaction = new Transaction(
            UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            "ACC-001",
            "DEPOSIT",
            new BigDecimal("1000.00"),
            "USD",
            "COMPLETED",
            null,
            null
        );

        transactionJson = """
            {
                "accountId": "ACC-001",
                "type": "DEPOSIT",
                "amount": 1000.00,
                "currency": "USD"
            }
            """;
    }

    @Test
    @Disabled("Superficie de práctica - probar endpoint POST /api/transactions")
    void testCreateTransaction_ReturnsCreated() throws Exception {
        when(transactionService.processTransaction(any(Transaction.class)))
            .thenReturn(sampleTransaction);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(sampleTransaction.id().toString()))
            .andExpect(jsonPath("$.accountId").value("ACC-001"))
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @Disabled("Superficie de práctica - probar validación de cuenta vacía")
    void testCreateTransaction_BlankAccountId_ReturnsBadRequest() throws Exception {
        String invalidJson = """
            {
                "accountId": "",
                "type": "DEPOSIT",
                "amount": 1000.00,
                "currency": "USD"
            }
            """;

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Disabled("Superficie de práctica - probar GET /api/transactions/{id}")
    void testGetTransactionById_ReturnsTransaction() throws Exception {
        UUID transactionId = sampleTransaction.id();

        when(transactionService.getTransactionById(transactionId))
            .thenReturn(sampleTransaction);

        mockMvc.perform(get("/api/transactions/{id}", transactionId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(transactionId.toString()))
            .andExpect(jsonPath("$.accountId").value("ACC-001"));
    }

    @Test
    @Disabled("Superficie de práctica - probar GET /api/transactions/{id} cuando no existe")
    void testGetTransactionById_NotFound_Returns404() throws Exception {
        UUID transactionId = UUID.randomUUID();

        when(transactionService.getTransactionById(transactionId))
            .thenReturn(null);

        mockMvc.perform(get("/api/transactions/{id}", transactionId))
            .andExpect(status().isNotFound());
    }

    @Test
    @Disabled("Superficie de práctica - probar GET /api/transactions?accountId={id}")
    void testGetTransactionsByAccountId_ReturnsList() throws Exception {
        String accountId = "ACC-001";

        when(transactionService.getTransactionsByAccountId(accountId))
            .thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/api/transactions")
                .param("accountId", accountId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].accountId").value(accountId));
    }

    @Test
    @Disabled("Superficie de práctica - probar filtrado por tipo de transacción")
    void testGetTransactionsByAccountIdAndType_ReturnsFilteredList() throws Exception {
        String accountId = "ACC-001";
        String type = "DEPOSIT";

        when(transactionService.getTransactionsByAccountIdAndType(accountId, type))
            .thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/api/transactions")
                .param("accountId", accountId)
                .param("type", type))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].type").value(type));
    }

    @Test
    @Disabled("Superficie de práctica - probar PATCH /api/transactions/{id}/status")
    void testUpdateTransactionStatus_ReturnsUpdated() throws Exception {
        UUID transactionId = sampleTransaction.id();
        String newStatus = "COMPLETED";

        Transaction updatedTransaction = new Transaction(
            transactionId,
            sampleTransaction.accountId(),
            sampleTransaction.type(),
            sampleTransaction.amount(),
            sampleTransaction.currency(),
            newStatus,
            null,
            null
        );

        when(transactionService.updateTransactionStatus(transactionId, newStatus))
            .thenReturn(updatedTransaction);

        mockMvc.perform(patch("/api/transactions/{id}/status", transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"COMPLETED\""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(newStatus));
    }
}