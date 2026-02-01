package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetCreateDTO;
// unused import removed
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetUpdateServiceTest {

    @Mock
    private AssetRepository repository;
    @Mock
    private AssetTypeRepository typeRepository;
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private AssetUpdateService service;

    @Test
    void update_Success() {
        long uuid = 10L;
        AssetCreateDTO dto = new AssetCreateDTO("Updated", "INV_NEW", "1", "2", null, null, "New Cond");
        Asset asset = new Asset();
        asset.setUuid(uuid);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(asset));
        when(typeRepository.findByUuid(1L)).thenReturn(Optional.of(new AssetType()));
        when(locationRepository.findByUuid(2L)).thenReturn(Optional.of(new Location()));
        
        Long resultId = service.update(uuid, dto);

        assertNotNull(resultId);
        verify(repository).saveAndFlush(asset);
    }
}
