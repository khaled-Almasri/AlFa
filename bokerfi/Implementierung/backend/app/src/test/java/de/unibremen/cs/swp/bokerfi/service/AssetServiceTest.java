package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository repository;

    // We don't need AssetMapper here for the handover method, but it is a constructor dependency.
    // If AssetService uses constructor injection, we might need a mock for it or rely on @InjectMocks.
    // @InjectMocks tries to create an instance. If constructor args are missing in mocks, it might fail or pass nulls.
    // AssetService(AssetRepository repository, AssetMapper mapper)
    // We should mock AssetMapper too to be safe, even if unused in handover().
    
    @Mock
    private de.unibremen.cs.swp.bokerfi.mapper.AssetMapper mapper;

    @Mock
    private de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssetService service;

    @Test
    void findAllForEmployee_ShouldReturnDTOsWithDeadline() {
        String email = "emp@test.de";
        de.unibremen.cs.swp.bokerfi.model.Assignment assignment = new de.unibremen.cs.swp.bokerfi.model.Assignment();
        Asset asset = new Asset();
        asset.setName("TestAsset");
        assignment.setAsset(asset);
        assignment.setReturnDeadline(java.time.LocalDateTime.now().plusDays(5));
        
        de.unibremen.cs.swp.bokerfi.dto.AssetDTO dto = new de.unibremen.cs.swp.bokerfi.dto.AssetDTO(
           1L, "TestAsset", de.unibremen.cs.swp.bokerfi.model.AssetStatus.ASSIGNED, "GOOD", "Type", "INV", null, null, null, null, null, null, null, null, null, null, null
        );

        when(assignmentRepository.findByPersonEmailAndReturnedAtIsNull(email)).thenReturn(java.util.List.of(assignment));
        when(mapper.toDto(asset)).thenReturn(dto);

        java.util.List<de.unibremen.cs.swp.bokerfi.dto.AssetDTO> result = service.findAllForEmployee(email);
        
        assertEquals(1, result.size());
        assertEquals("TestAsset", result.get(0).name());
        assertEquals(assignment.getReturnDeadline(), result.get(0).returnDeadline());
    }

    @Test
    void handover_ShouldThrowException_WhenAssetNotFound() {
        long uuid = 999L;
        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

        BokerfiException ex = assertThrows(BokerfiException.class, () -> service.handover(uuid));
        assertEquals(ErrorCode.ENTITY_DOES_NOT_EXIST, ex.getErrorCode());
    }

    @Test
    void handover_ShouldSetStatusAssigned_WhenReadyForInitialPickup() {
        long uuid = 100L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.READY_FOR_INITIAL_PICKUP);
        
        when(repository.findByUuid(uuid)).thenReturn(Optional.of(asset));
        when(repository.save(asset)).thenReturn(asset); // save returns entity

        service.handover(uuid);

        assertEquals(AssetStatus.ASSIGNED, asset.getStatus());
        verify(repository).save(asset);
        verify(repository).flush();
    }

    @Test
    void handover_ShouldSetStatusAssigned_WhenReadyForRepairPickup() {
        long uuid = 101L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.READY_FOR_REPAIR_PICKUP);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(asset));
        
        service.handover(uuid);

        assertEquals(AssetStatus.ASSIGNED, asset.getStatus());
        verify(repository).save(asset);
    }

    @Test
    void handover_ShouldDoNothingOrPass_WhenAlreadyAssigned() {
        // Logic says: if status == ASSIGNED, nothing in if block happens. 
        // else if status != ASSIGNED -> throw CONFLICT.
        // So if ASSIGNED, it should pass without error and save.
        
        long uuid = 102L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.ASSIGNED);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        service.handover(uuid);

        assertEquals(AssetStatus.ASSIGNED, asset.getStatus());
        verify(repository).save(asset);
    }

    @Test
    @SuppressWarnings("null")
    void handover_ShouldThrowConflict_WhenStatusIsInvalid() {
        // E.g. IN_STOCK
        long uuid = 103L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.IN_STOCK);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        BokerfiException ex = assertThrows(BokerfiException.class, () -> service.handover(uuid));
        assertEquals(ErrorCode.CONFLICT, ex.getErrorCode());
        
        verify(repository, never()).save(any());
    }

    @Test
    void restock_ShouldSetStatusInStock_WhenStatusIsNotInStock() {
        long uuid = 200L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.ASSIGNED);
        asset.setAssignedPerson(new de.unibremen.cs.swp.bokerfi.model.Person());

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        service.restock(uuid);

        assertEquals(AssetStatus.IN_STOCK, asset.getStatus());
        assertEquals(null, asset.getAssignedPerson());
        verify(repository).save(asset);
        verify(repository).flush();
    }

    @Test
    @SuppressWarnings("null")
    void restock_ShouldThrowConflict_WhenAlreadyInStock() {
        long uuid = 201L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.IN_STOCK);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        BokerfiException ex = assertThrows(BokerfiException.class, () -> service.restock(uuid));
        assertEquals(ErrorCode.CONFLICT, ex.getErrorCode());
        
        verify(repository, never()).save(any());
    }

    @Test
    @SuppressWarnings("null")
    void search_ShouldFilterByAssigneeAndLocation() {
        // Arrange
        String assigneeId = "10";
        String locationId = "20";
        
        de.unibremen.cs.swp.bokerfi.model.Person person = new de.unibremen.cs.swp.bokerfi.model.Person();
        person.setId(10L); // Assuming ID matches logic or we mock getter? 
        // AssetService.search uses: String.valueOf(asset.getAssignedPerson().getUuid()).equals(assigneeUuid)
        // Wait, the filter uses UUID from entity? 
        // "String.valueOf(asset.getAssignedPerson().getUuid()).equals(assigneeUuid)"
        // Person extends DBEntity? Yes. DBEntity has uuid/id. 
        // Let's assume Person has a UUID field or inherits it. 
        // BaseService uses 'uuid' usually. DBEntity has 'id' (Long) and maybe 'uuid' (Long)? Review DBEntity if needed.
        // AssetDeleteServiceTest uses 'uuid = 1L'.
        
        // Let's verify DBEntity structure or mock behavior.
        // Actually, if I can't easily set UUID on Person/Location, I should mock the getters if spying, or just set fields if accessible.
        // Checking AssetService line 84: String.valueOf(asset.getAssignedPerson().getUuid()).equals(assigneeUuid)
        // So I need to ensure getUuid() returns what matches 'assigneeId'.
        
        Asset assetMatch = new Asset();
        de.unibremen.cs.swp.bokerfi.model.Person p = mock(de.unibremen.cs.swp.bokerfi.model.Person.class);
        when(p.getUuid()).thenReturn(10L);
        assetMatch.setAssignedPerson(p);
        
        de.unibremen.cs.swp.bokerfi.model.Location l = mock(de.unibremen.cs.swp.bokerfi.model.Location.class);
        when(l.getUuid()).thenReturn(20L);
        assetMatch.setLocation(l);
        
        Asset assetNoMatch = new Asset();
        assetNoMatch.setAssignedPerson(null);
        
        
        when(repository.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(java.util.List.of(assetMatch, assetNoMatch));
        
        de.unibremen.cs.swp.bokerfi.dto.AssetDTO mockDto = mock(de.unibremen.cs.swp.bokerfi.dto.AssetDTO.class);
        when(mapper.toDto(assetMatch)).thenReturn(mockDto);

        // Act
        java.util.List<de.unibremen.cs.swp.bokerfi.dto.AssetDTO> result = service.search(assigneeId, locationId, null, null, org.springframework.data.domain.Sort.unsorted());

        // Assert
        assertEquals(1, result.size());
    }
}
