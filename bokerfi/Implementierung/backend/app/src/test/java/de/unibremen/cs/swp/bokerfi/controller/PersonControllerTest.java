package de.unibremen.cs.swp.bokerfi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.model.Role;
import de.unibremen.cs.swp.bokerfi.service.PersonCreateService;
import de.unibremen.cs.swp.bokerfi.service.PersonDeleteService;
import de.unibremen.cs.swp.bokerfi.service.PersonService;
import de.unibremen.cs.swp.bokerfi.service.PersonUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Objects;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@Import(de.unibremen.cs.swp.bokerfi.auth.SecurityConfig.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;
    @MockitoBean
    private PersonCreateService personCreateService;
    @MockitoBean
    private PersonUpdateService personUpdateService;
    @MockitoBean
    private PersonDeleteService personDeleteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPersons_Success() throws Exception {
        when(personService.findAll()).thenReturn(List.of(new PersonDTO("e", "f", "l", "1", Role.ADMIN, true, 1L, 0L, Collections.emptyList())));

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getPersons_Forbidden() throws Exception {
        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPerson_Success() throws Exception {
        PersonCreateDTO dto = new PersonCreateDTO("new@test.de", "F", "L", "22", "pass", Role.EMPLOYEE, Collections.emptyList());
        PersonDTO response = new PersonDTO("new@test.de", "F", "L", "22", Role.EMPLOYEE, true, 100L, 0L, Collections.emptyList());

        when(personCreateService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/persons")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isCreated());
    }
}
