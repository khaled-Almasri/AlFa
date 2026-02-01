package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.AssetDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.mapper.AssetMapper;
import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetRepository;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class AssetCreateServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AssetTypeRepository typeRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private AssetMapper assetMapper;
    @Mock
    private HistoryService historyService;

    @InjectMocks
    private AssetCreateService service;

    @Test
    void create_Success() {
        AssetCreateDTO dto = new AssetCreateDTO("Asset1", "INV123", "1", "2", LocalDate.now(), LocalDate.now(), "Good");
        AssetType type = new AssetType();
        Location location = new Location();
        Asset asset = new Asset();
        // AssetDTO constructor: (uuid, name, status, condition, typeName, inventoryNumber, purchaseDate, warrantyEnd, assigneeUuid, locationUuid, typeUuid, location, type, assignedPerson, addedAt, returnDeadline)
        // From Step 719, it's a huge record.
        // I'll assume mock toDto handles it.
        AssetDTO responseDto = new AssetDTO(1L, "Asset1", de.unibremen.cs.swp.bokerfi.model.AssetStatus.IN_STOCK, "Good", "Type1", "INV123", LocalDate.now(), LocalDate.now(), null, "2", "1", null, null, null, null, null, null);

        when(typeRepository.findByUuid(1L)).thenReturn(Optional.of(type));
        when(locationRepository.findByUuid(2L)).thenReturn(Optional.of(location));
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);
        when(assetMapper.toDto(asset)).thenReturn(responseDto);

        AssetDTO result = service.create(dto);

        assertNotNull(result);
        verify(historyService).logEvent(eq(asset), eq("CREATED"), any(), (Person) isNull());
    }

    @Test
    void create_TypeNotFound() {
        AssetCreateDTO dto = new AssetCreateDTO("Asset1", "INV123", "1", "2", null, null, null);
        when(typeRepository.findByUuid(1L)).thenReturn(Optional.empty());
        when(typeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BokerfiException.class, () -> service.create(dto));
    }
}
