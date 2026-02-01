package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetReportDefectServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private HistoryService historyService;

    private AssetReportDefectService service;

    @BeforeEach
    void setUp() {
        service = new AssetReportDefectService(assetRepository, personRepository, emailService, historyService, "admin@test.de");
    }

    @Test
    void reportDefect_Success() {
        long assetUuid = 123L;
        long reporterUuid = 456L;
        String desc = "Broken Screen";

        Asset asset = new Asset();
        asset.setName("Laptop");
        asset.setInventoryNumber("INV-001");

        Person reporter = new Person();
        reporter.setFirstName("John");
        reporter.setLastName("Doe");
        reporter.setPersonnelNumber("P-100");

        when(assetRepository.findByUuid(assetUuid)).thenReturn(Optional.of(asset));
        when(personRepository.findByUuid(reporterUuid)).thenReturn(Optional.of(reporter));

        service.reportDefect(assetUuid, reporterUuid, desc);

        verify(emailService).sendMail(eq("admin@test.de"), anyString(), anyString());
        verify(historyService).logEvent(eq(asset), eq("DEFECT_REPORTED"), anyString(), eq(reporter));
    }

    @Test
    void reportDefect_AssetNotFound() {
        long assetUuid = 123L;
        when(assetRepository.findByUuid(assetUuid)).thenReturn(Optional.empty());

        assertThrows(BokerfiException.class, () -> service.reportDefect(assetUuid, 1L, "desc"));
    }

    @Test
    void reportDefect_PersonNotFound() {
        long assetUuid = 123L;
        long reporterUuid = 456L;

        when(assetRepository.findByUuid(assetUuid)).thenReturn(Optional.of(new Asset()));
        when(personRepository.findByUuid(reporterUuid)).thenReturn(Optional.empty());

        assertThrows(BokerfiException.class, () -> service.reportDefect(assetUuid, reporterUuid, "desc"));
    }
}
