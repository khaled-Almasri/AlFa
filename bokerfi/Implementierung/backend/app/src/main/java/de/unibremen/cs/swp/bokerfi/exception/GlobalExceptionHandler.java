package de.unibremen.cs.swp.bokerfi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

/**
 * Globaler Exception-Handler für die REST-Schnittstelle.
 * Fängt Exceptions ab und wandelt sie in standardisierte Fehlerantworten um.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";

    @ExceptionHandler(BokerfiException.class)
    public ResponseEntity<Object> handleBokerfiException(BokerfiException ex) {

        if (ex.getErrorCode() == ErrorCode.RESERVATION_CONFLICT || 
            ex.getErrorCode() == ErrorCode.VERSION_CONFLICT ||
            ex.getErrorCode() == ErrorCode.CONFLICT) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            ERROR_KEY, ex.getErrorCode().name(),
                            MESSAGE_KEY, ex.getMessage()
                    ));
        }

        if (ex.getErrorCode() == ErrorCode.ENTITY_DOES_NOT_EXIST) {
            return ResponseEntity.status(404).body(Map.of(
                    ERROR_KEY, ex.getErrorCode().name(),
                    MESSAGE_KEY, ex.getMessage()
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                ERROR_KEY, ex.getErrorCode().name(),
                MESSAGE_KEY, ex.getMessage()
        ));
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFound(Exception ex) {

        return ResponseEntity.status(404).body(Map.of(
                ERROR_KEY, "NOT_FOUND",
                MESSAGE_KEY, ex.getMessage()
        ));
    }

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(org.springframework.web.HttpRequestMethodNotSupportedException ex) {

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(Map.of(
                ERROR_KEY, "METHOD_NOT_ALLOWED",
                MESSAGE_KEY, ex.getMessage()
        ));
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        ERROR_KEY, "ENTITY_ALREADY_EXISTS",
                        MESSAGE_KEY, "Data integrity violation: " + ex.getMessage()
                ));
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            jakarta.validation.ConstraintViolationException.class,
            jakarta.persistence.PersistenceException.class,
            org.springframework.transaction.TransactionSystemException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Object> handleValidationExceptions(Exception ex) {

        // Unwrap TransactionSystemException if possible
        Throwable cause = ex;
        if (ex instanceof org.springframework.transaction.TransactionSystemException && ex.getCause() != null) {
            cause = ex.getCause();
        }
        
        log.warn("Validation Error caught: {}: {}", ex.getClass().getSimpleName(), cause.getMessage());
        log.debug("Stack trace:", ex);

        return ResponseEntity.badRequest().body(Map.of(
                ERROR_KEY, "BAD_REQUEST",
                MESSAGE_KEY, "Validation error: " + cause.getMessage()
        ));
    }

    @ExceptionHandler({
            org.springframework.security.access.AccessDeniedException.class,
            org.springframework.security.authorization.AuthorizationDeniedException.class
    })
    public ResponseEntity<Object> handleAccessDenied(Exception ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                ERROR_KEY, "FORBIDDEN",
                MESSAGE_KEY, ex.getMessage() != null ? ex.getMessage() : "Access Denied"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {

        // Log the full stack trace for debugging
        log.error("Unhandled exception caught", ex);
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        ERROR_KEY, "INTERNAL_SERVER_ERROR",
                        MESSAGE_KEY, ex.getMessage() != null ? ex.getMessage() : "Unknown error"
                ));
    }
}