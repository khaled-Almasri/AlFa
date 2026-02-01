package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.infrastructure.EmailService;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetStatus;
import de.unibremen.cs.swp.bokerfi.model.Assignment;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

// import removed
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class AssetAssignServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private HistoryService historyService;

    @InjectMocks
    private AssetAssignService service;

    @Test
    void assign_Success() {
        long assetUuid = 1L;
        long personUuid = 2L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.IN_STOCK);
        Person person = new Person();
        person.setUuid(personUuid);
        person.setEmail("test@test.de");

        when(assetRepository.findByUuid(assetUuid)).thenReturn(Optional.of(asset));
        when(personRepository.findByUuid(personUuid)).thenReturn(Optional.of(person));
        
        service.assign(assetUuid, personUuid, null);

        verify(assignmentRepository).save(any(Assignment.class));
        verify(emailService).sendMail(eq("test@test.de"), any(), any());
        verify(historyService).logEvent(eq(asset), eq("ASSIGNMENT_INITIATED"), any(), (Person) isNull());
    }

    @Test
    void assign_WrongStatus() {
        long assetUuid = 1L;
        long personUuid = 2L;
        Asset asset = new Asset();
        asset.setStatus(AssetStatus.ASSIGNED);

        when(assetRepository.findByUuid(assetUuid)).thenReturn(Optional.of(asset));

        assertThrows(BokerfiException.class, () -> service.assign(assetUuid, personUuid, null));
    }
}
