package de.unibremen.cs.swp.bokerfi.service;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetMaintenanceFinishServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private HistoryService historyService;

    private AssetMaintenanceFinishService service;

    @BeforeEach
    void setUp() {
        service = new AssetMaintenanceFinishService(assetRepository, emailService, historyService, "admin@test.de");
    }

    @Test
    void finishMaintenance_Repair_Success() {
        long uuid = 1L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.MAINTENANCE_REPAIR);
        asset.setName("RepairedItem");
        Person owner = new Person();
        owner.setEmail("owner@test.de");
        asset.setAssignedPerson(owner);

        when(assetRepository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        service.finishMaintenance(uuid);

        assertEquals(AssetStatus.READY_FOR_REPAIR_PICKUP, asset.getStatus());
        verify(emailService).sendMail(eq("owner@test.de"), any(), any());
    }

    @Test
    void finishMaintenance_Final_Success() {
        long uuid = 2L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.MAINTENANCE_FINAL);
        asset.setName("OldItem");

        when(assetRepository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        service.finishMaintenance(uuid);

        assertEquals(AssetStatus.RETIRED, asset.getStatus());
        verify(emailService).sendMail(eq("admin@test.de"), any(), any());
    }
}
