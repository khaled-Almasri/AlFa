package de.unibremen.cs.swp.bokerfi.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void handleBokerfiException_Conflict() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Conflict occurred"));
    }

    @Test
    void handleBokerfiException_NotFound() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("ENTITY_DOES_NOT_EXIST"));
    }

    @Test
    void handleBokerfiException_BadRequest() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("CONSTRAINT_VIOLATION"));
    }

    @Test
    void handleNoResourceFound() throws Exception {
        mockMvc.perform(get("/test/no-resource"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void handleMethodNotSupported() throws Exception {
        mockMvc.perform(get("/test/method-not-supported"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.error").value("METHOD_NOT_ALLOWED"));
    }

    @Test
    void handleDataIntegrityViolation() throws Exception {
        mockMvc.perform(get("/test/data-integrity"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("ENTITY_ALREADY_EXISTS"));
    }

    @Test
    void handleValidationExceptions_IllegalArgument() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }

    @Test
    void handleAccessDenied() throws Exception {
        mockMvc.perform(get("/test/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));
    }

    @Test
    void handleGeneralException() throws Exception {
        mockMvc.perform(get("/test/general"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"));
    }

    @RestController
    static class TestController {
        @GetMapping("/test/conflict")
        void throwConflict() {
            throw new BokerfiException(ErrorCode.CONFLICT, "Conflict occurred");
        }

        @GetMapping("/test/not-found")
        void throwNotFound() {
            throw new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "Entity not found");
        }

        @GetMapping("/test/bad-request")
        void throwBadRequest() {
            throw new BokerfiException(ErrorCode.CONSTRAINT_VIOLATION, "Constraint violated");
        }

        @GetMapping("/test/no-resource")
        void throwNoResource() throws NoResourceFoundException {
            throw new NoResourceFoundException(java.util.Objects.requireNonNull(HttpMethod.GET), "resource");
        }

        @GetMapping("/test/method-not-supported")
        void throwMethodNotSupported() throws HttpRequestMethodNotSupportedException {
            throw new HttpRequestMethodNotSupportedException("POST", Collections.singleton("GET"));
        }

        @GetMapping("/test/data-integrity")
        void throwDataIntegrity() {
            throw new DataIntegrityViolationException("Duplicate key");
        }

        @GetMapping("/test/illegal-argument")
        void throwIllegalArgument() {
            throw new IllegalArgumentException("Invalid argument");
        }

        @GetMapping("/test/access-denied")
        void throwAccessDenied() {
            throw new AccessDeniedException("Access denied");
        }

        @GetMapping("/test/general")
        void throwGeneral() {
            throw new RuntimeException("Unexpected error");
        }
    }
}
