package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetHistory;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetHistoryRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class HistoryServiceTest {

    @Mock
    private AssetHistoryRepository historyRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private HistoryService historyService;

    @Test
    void logEvent_WithPerson_Success() {
        Asset asset = new Asset();
        historyService.logEvent(asset, "TEST", "Desc", (Person) null);
        verify(historyRepository).save(any(AssetHistory.class));
    }

    @Test
    void logEvent_WithUuid_NullUuid() {
        Asset asset = new Asset();
        historyService.logEvent(asset, "TEST", "Desc", (Long) null);
        verify(historyRepository).save(any(AssetHistory.class));
    }

    @Test
    void logEvent_WithEmail_NotFound() {
        Asset asset = new Asset();
        when(personRepository.findByEmail("unknown@test.de")).thenReturn(Optional.empty());
        historyService.logEvent(asset, "TEST", "Desc", "unknown@test.de");
        verify(historyRepository).save(any(AssetHistory.class));
    }

    @Test
    void logEvent_WithEmail_NullEmail() {
        Asset asset = new Asset();
        historyService.logEvent(asset, "TEST", "Desc", (String) null);
        verify(historyRepository).save(any(AssetHistory.class));
    }

    @Test
    void logEvent_WithUuid_Success() {
        Asset asset = new Asset();
        when(personRepository.findById(100L)).thenReturn(Optional.of(new Person()));
        historyService.logEvent(asset, "TEST", "Desc", 100L);
        verify(historyRepository).save(any(AssetHistory.class));
    }
}
