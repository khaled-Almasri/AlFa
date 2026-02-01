package de.unibremen.cs.swp.bokerfi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.model.Role;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import java.util.Objects;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(de.unibremen.cs.swp.bokerfi.auth.SecurityConfig.class) // Import Security if needed, or mock it broadly
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonRepository repository;

    @MockitoBean
    private PersonMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_Success() throws Exception {
        Person person = new Person();
        person.setEmail("test@test.de");
        person.setPassword("password");
        person.setRole(Role.EMPLOYEE);

        when(repository.findByEmail(anyString())).thenReturn(Optional.of(person));

        Map<String, String> credentials = Map.of("email", "test@test.de", "password", "password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(credentials)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_Failure_WrongPassword() throws Exception {
        Person person = new Person();
        person.setEmail("test@test.de");
        person.setPassword("password");

        when(repository.findByEmail(anyString())).thenReturn(Optional.of(person));

        Map<String, String> credentials = Map.of("email", "test@test.de", "password", "wrong");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(credentials)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void me_Success() throws Exception {
        Person person = new Person();
        person.setEmail("user");
        PersonDTO dto = new PersonDTO("user", "F", "L", "123", Role.EMPLOYEE, true, 1L, 0L, Collections.emptyList());

        when(repository.findByEmail("user")).thenReturn(Optional.of(person)); // MockUser default name is "user"
        when(repository.findByPersonnelNumber("user")).thenReturn(Optional.empty()); // Fallback
        when(mapper.toDto(person)).thenReturn(dto);

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user"));
    }

    @Test
    @WithMockUser(username = "test@test.de")
    void changePassword_Success() throws Exception {
        Person person = new Person();
        person.setEmail("test@test.de");
        person.setPassword("oldPass");

        when(repository.findByEmail("test@test.de")).thenReturn(Optional.of(person));

        Map<String, String> body = Map.of(
            "currentPassword", "oldPass",
            "newPassword", "newPass",
            "confirmPassword", "newPass"
        );

        mockMvc.perform(post("/api/auth/change-password")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(body)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());

        assert(person.getPassword().equals("newPass"));
    }

    @Test
    @WithMockUser(username = "test@test.de")
    void changePassword_Failure_WrongCurrent() throws Exception {
        Person person = new Person();
        person.setEmail("test@test.de");
        person.setPassword("oldPass");

        when(repository.findByEmail("test@test.de")).thenReturn(Optional.of(person));

        Map<String, String> body = Map.of(
            "currentPassword", "wrongPass",
            "newPassword", "newPass"
        );

        mockMvc.perform(post("/api/auth/change-password")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(body)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isBadRequest());
    }
}
