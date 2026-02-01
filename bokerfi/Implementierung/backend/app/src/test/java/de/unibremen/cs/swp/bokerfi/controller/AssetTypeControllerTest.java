package de.unibremen.cs.swp.bokerfi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibremen.cs.swp.bokerfi.dto.AssetTypeCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.AssetTypeDTO;
import de.unibremen.cs.swp.bokerfi.service.AssetTypeCreateService;
import de.unibremen.cs.swp.bokerfi.service.AssetTypeDeleteService;
import de.unibremen.cs.swp.bokerfi.service.AssetTypeService;
import de.unibremen.cs.swp.bokerfi.service.AssetTypeUpdateService;
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

@WebMvcTest(AssetTypeController.class)
@Import(de.unibremen.cs.swp.bokerfi.auth.SecurityConfig.class)
class AssetTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetTypeService service;
    @MockitoBean
    private AssetTypeCreateService createService;
    @MockitoBean
    private AssetTypeUpdateService updateService;
    @MockitoBean
    private AssetTypeDeleteService deleteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_Success() throws Exception {
        when(service.findAll()).thenReturn(List.of(new AssetTypeDTO(1L, 100L, "Type1", "Desc")));

        mockMvc.perform(get("/api/assetTypes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void create_Success() throws Exception {
        AssetTypeCreateDTO dto = new AssetTypeCreateDTO("Type1", "Desc");
        AssetTypeDTO response = new AssetTypeDTO(1L, 100L, "Type1", "Desc");

        when(createService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/assetTypes")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isCreated());
    }
}
