package com.fintech.cleanarchitecture.infrastructure.config;



import com.fintech.cleanarchitecture.application.usecase.TransaccionNoEncontradaException;
import com.fintech.cleanarchitecture.application.usecase.ValidacionTransaccionException;
import com.fintech.cleanarchitecture.application.usecase.ProcessTransactionUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    @ExceptionHandler(ProcessTransactionUseCase.ValidacionTransaccionException.class)
    public ResponseEntity<ErrorResponse> handleValidacionTransaccion(
            ProcessTransactionUseCase.ValidacionTransaccionException ex,
            WebRequest request) {
        logger.warn("Error de validación en transacción: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ProcessTransactionUseCase.TransaccionNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleTransaccionNoEncontrada(
            ProcessTransactionUseCase.TransaccionNoEncontradaException ex,
            WebRequest request) {
        logger.warn("Transacción no encontrada: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "TRANSACTION_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        String errores = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        logger.warn("Errores de validación en request: {}", errores);
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "REQUEST_VALIDATION_ERROR",
            errores,
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request) {
        logger.warn("Argumento ilegal: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_ARGUMENT",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {
        logger.error("Error interno del servidor: {}", ex.getMessage(), ex);
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_SERVER_ERROR",
            "Ocurrió un error interno. Por favor, contacte al administrador.",
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    public record ErrorResponse(
        int status,
        String code,
        String message,
        LocalDateTime timestamp,
        String path
    ) {}
}