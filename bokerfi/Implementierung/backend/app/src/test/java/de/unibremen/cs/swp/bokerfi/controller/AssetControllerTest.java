package de.unibremen.cs.swp.bokerfi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibremen.cs.swp.bokerfi.dto.AssetCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.AssetDTO;
import de.unibremen.cs.swp.bokerfi.service.*;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import de.unibremen.cs.swp.bokerfi.dto.AssetAssignDTO;
import java.util.Optional;

@WebMvcTest(AssetController.class)
@Import(de.unibremen.cs.swp.bokerfi.auth.SecurityConfig.class)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AssetService assetService;
    @MockitoBean
    private AssetCreateService createService;
    @MockitoBean
    private AssetUpdateService updateService;
    @MockitoBean
    private AssetDeleteService deleteService;
    @MockitoBean
    private AssetAssignService assetAssignService;
    @MockitoBean
    private AssetReturnService assetReturnService;
    @MockitoBean
    private AssetLostService assetLostService;
    @MockitoBean
    private AssetMaintenanceStartService maintenanceStartService;
    @MockitoBean
    private AssetMaintenanceFinishService maintenanceFinishService;
    @MockitoBean
    private AssetReportDefectService reportDefectService;
    @MockitoBean
    private AssetRetireService retireService;
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_WithAliasesAndSorting() throws Exception {
        when(assetService.search(any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/assets")
                .param("assignee", "123")
                .param("location_id", "456")
                .param("SortBy", "inventoryNumber")
                .param("sortDirection", "DESC"))
                .andExpect(status().isOk());
                
        // Verify service called with resolved parameters
        verify(assetService).search(
            org.mockito.ArgumentMatchers.eq("123"), 
            org.mockito.ArgumentMatchers.eq("456"), 
            any(), 
            any(), 
            any(org.springframework.data.domain.Sort.class)
        );
    }


    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void findAll_Employee_Success() throws Exception {
        when(assetService.findAllForEmployee(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_Admin_Success() throws Exception {
        when(assetService.search(any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void create_Success() throws Exception {
        AssetCreateDTO dto = new AssetCreateDTO("A", "I", "1", "2", null, null, "C");
        AssetDTO response = new AssetDTO(1L, "A", de.unibremen.cs.swp.bokerfi.model.AssetStatus.IN_STOCK, "C", "T", "I", null, null, null, null, null, null, null, null, null, null, null);

        when(createService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/assets")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_Success() throws Exception {
        AssetCreateDTO dto = new AssetCreateDTO("A", "I", "1", "2", null, null, "C");
        when(updateService.update(any(Long.class), any())).thenReturn(1L);
        when(assetService.findByUuid(1L)).thenReturn(Optional.of(new AssetDTO(1L, "A", de.unibremen.cs.swp.bokerfi.model.AssetStatus.IN_STOCK, "C", "T", "I", null, null, null, null, null, null, null, null, null, null, null)));

        mockMvc.perform(put("/api/assets/1")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_NotFound() throws Exception {
        AssetCreateDTO dto = new AssetCreateDTO("A", "I", "1", "2", null, null, "C");
        when(updateService.update(any(Long.class), any())).thenReturn(99L);
        when(assetService.findByUuid(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/assets/99")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_Success() throws Exception {
        mockMvc.perform(delete("/api/assets/1")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());
        
        verify(deleteService).delete(1L);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void reportDefect_Success() throws Exception {
        java.util.Map<String, Object> body = java.util.Map.of("description", "Broken", "reporterUuid", 100);
        
        mockMvc.perform(post("/api/assets/1/defect")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(body)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isOk());
        
        verify(reportDefectService).reportDefect(1L, 100L, "Broken");
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void reportDefect_MissingDescription_ShouldFail() throws Exception {
        java.util.Map<String, Object> body = java.util.Map.of("reporterUuid", 100);
        
        mockMvc.perform(post("/api/assets/1/defect")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(body)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void assign_Success() throws Exception {
        AssetAssignDTO dto = new AssetAssignDTO(100L, null);
        when(assetAssignService.assign(1L, 100L, null)).thenReturn(1L);

        mockMvc.perform(post("/api/assets/1/assign")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void returnAsset_Success() throws Exception {
        mockMvc.perform(post("/api/assets/1/return")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());

        verify(assetReturnService).returnAsset(1L);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void markLost_Success() throws Exception {
        mockMvc.perform(post("/api/assets/1/mark-lost")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());

        verify(assetLostService).markLost(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void confirmLost_Success() throws Exception {
        mockMvc.perform(post("/api/assets/1/confirm-lost")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());

        verify(assetLostService).confirmLoss(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void startMaintenance_Success() throws Exception {
        mockMvc.perform(post("/api/assets/1/maintenance/start")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());

        verify(maintenanceStartService).startMaintenance(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void finishMaintenance_Success() throws Exception {
        mockMvc.perform(post("/api/assets/1/maintenance/finish")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());

        verify(maintenanceFinishService).finishMaintenance(1L);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void restock_Success() throws Exception {
        mockMvc.perform(post("/api/assets/1/restock")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());
                
        verify(assetService).restock(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_Success() throws Exception {
        when(assetService.findByUuid(1L)).thenReturn(Optional.of(new AssetDTO(1L, "A", de.unibremen.cs.swp.bokerfi.model.AssetStatus.IN_STOCK, "C", "T", "I", null, null, null, null, null, null, null, null, null, null, null)));

        mockMvc.perform(get("/api/assets/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_NotFound() throws Exception {
        when(assetService.findByUuid(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/assets/99"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = "MANAGER")
    void handover_Success() throws Exception {
        mockMvc.perform(post("/api/assets/1/handover")
                        .with(Objects.requireNonNull(csrf())))
                .andExpect(status().isNoContent());
                
        verify(assetService).handover(1L);
    }
}
