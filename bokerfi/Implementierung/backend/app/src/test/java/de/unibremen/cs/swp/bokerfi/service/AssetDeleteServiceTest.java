package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssignmentRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssetHistoryRepository;
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
class AssetDeleteServiceTest {

    @Mock
    private AssetRepository repository;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private AssetHistoryRepository assetHistoryRepository;

    @InjectMocks
    private AssetDeleteService service;

    @Test
    void delete_Success() {
        long uuid = 1L;
        Asset asset = new Asset();

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(asset));

        service.delete(uuid);

        verify(assignmentRepository).deleteAll(any());
        verify(assetHistoryRepository).deleteAll(any());
        verify(repository).delete(asset);
    }
}
