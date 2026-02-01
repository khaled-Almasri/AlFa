package de.unibremen.cs.swp.bokerfi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibremen.cs.swp.bokerfi.dto.LocationCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
import de.unibremen.cs.swp.bokerfi.service.LocationCreateService;
import de.unibremen.cs.swp.bokerfi.service.LocationDeleteService;
import de.unibremen.cs.swp.bokerfi.service.LocationService;
import de.unibremen.cs.swp.bokerfi.service.LocationUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
@Import(de.unibremen.cs.swp.bokerfi.auth.SecurityConfig.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationService service;
    @MockitoBean
    private LocationCreateService createService;
    @MockitoBean
    private LocationUpdateService updateService;
    @MockitoBean
    private LocationDeleteService deleteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_Success() throws Exception {
        when(service.findAll()).thenReturn(List.of(new LocationDTO(1L, 100L, "Loc1")));

        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void create_Success() throws Exception {
        LocationCreateDTO dto = new LocationCreateDTO("New Loc");
        LocationDTO response = new LocationDTO(1L, 100L, "New Loc");

        when(createService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/locations")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isCreated());
    }
}
