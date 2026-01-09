package de.unibremen.cs.swp.bokerfi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BokerfiException.class)
    public ResponseEntity<Object> handleBokerfiException(BokerfiException ex) {

        if (ex.getErrorCode() == ErrorCode.ENTITY_DOES_NOT_EXIST) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", ex.getErrorCode().name(),
                    "message", ex.getMessage()
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "error", ex.getErrorCode().name(),
                "message", ex.getMessage()
        ));
    }

}