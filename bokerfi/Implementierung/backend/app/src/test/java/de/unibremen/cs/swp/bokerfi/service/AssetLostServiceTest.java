package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class AssetLostServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private HistoryService historyService;

    private AssetLostService service;

    @BeforeEach
    void setUp() {
        service = new AssetLostService(assetRepository, emailService, historyService, "admin@test.de");
    }

    @Test
    void markLost_Success() {
        long uuid = 1L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.ASSIGNED);
        asset.setName("LostItem");

        when(assetRepository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        service.markLost(uuid);

        assertEquals(AssetStatus.MARKED_LOST, asset.getStatus());
        verify(emailService).sendMail(eq("admin@test.de"), any(), any());
        verify(historyService).logEvent(eq(asset), eq("MARKED_LOST"), any(), (Person) isNull());
    }

    @Test
    void markLost_Conflict_WhenNotAssigned() {
        long uuid = 2L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.IN_STOCK);

        when(assetRepository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        assertThrows(BokerfiException.class, () -> service.markLost(uuid));
    }

    @Test
    void confirmLoss_Success() {
        long uuid = 3L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.MARKED_LOST);

        when(assetRepository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        service.confirmLoss(uuid);

        assertEquals(AssetStatus.LOST, asset.getStatus());
        verify(historyService).logEvent(eq(asset), eq("LOST_CONFIRMED"), any(), (Person) isNull());
    }
}
