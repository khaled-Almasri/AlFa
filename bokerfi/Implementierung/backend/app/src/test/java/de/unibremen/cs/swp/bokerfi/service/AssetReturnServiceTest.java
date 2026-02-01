package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.model.Assignment;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetReturnServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private HistoryService historyService;

    @InjectMocks
    private AssetReturnService service;

    @Test
    void returnAsset_Success() {
        long uuid = 1L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.ASSIGNED);
        Assignment assignment = new Assignment();

        when(assetRepository.findByUuid(uuid)).thenReturn(Optional.of(asset));
        when(assignmentRepository.findByAssetAndReturnedAtIsNull(asset)).thenReturn(Optional.of(assignment));

        service.returnAsset(uuid);

        assertEquals(AssetStatus.RETURNED_FINAL, asset.getStatus());
        verify(historyService).logEvent(eq(asset), eq("RETURNED_FINAL"), any(), (Person) isNull());
    }

    @Test
    void returnAsset_NotAssigned() {
        long uuid = 1L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.IN_STOCK);

        when(assetRepository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        assertThrows(BokerfiException.class, () -> service.returnAsset(uuid));
    }
}
