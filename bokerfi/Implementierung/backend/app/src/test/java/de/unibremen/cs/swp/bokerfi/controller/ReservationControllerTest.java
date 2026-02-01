package de.unibremen.cs.swp.bokerfi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibremen.cs.swp.bokerfi.dto.ReservationCreateDTO;
import de.unibremen.cs.swp.bokerfi.model.Reservation;
import de.unibremen.cs.swp.bokerfi.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@Import(de.unibremen.cs.swp.bokerfi.auth.SecurityConfig.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService service;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_Success() throws Exception {
        Reservation reservation = new Reservation();
        when(service.findAll()).thenReturn(List.of(reservation));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_Success() throws Exception {
        ReservationCreateDTO dto = new ReservationCreateDTO(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "100",
                "200",
                null
        );
        when(service.create(any(ReservationCreateDTO.class))).thenReturn(123L);

        mockMvc.perform(post("/api/reservations")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(123));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_Success() throws Exception {
        mockMvc.perform(delete("/api/reservations/1")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isOk());

        verify(service).delete(1L);
    }
}
