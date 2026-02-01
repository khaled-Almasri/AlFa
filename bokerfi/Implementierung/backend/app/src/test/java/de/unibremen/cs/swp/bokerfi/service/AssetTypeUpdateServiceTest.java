package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetTypeCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.AssetTypeDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.mapper.AssetTypeMapper;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetTypeUpdateServiceTest {

    @Mock
    private AssetTypeRepository repository;
    @Mock
    private AssetTypeMapper mapper;

    @InjectMocks
    private AssetTypeUpdateService service;

    @Test
    void update_Success() {
        long uuid = 1L;
        AssetTypeCreateDTO dto = new AssetTypeCreateDTO("Updated", "Desc");
        AssetType entity = new AssetType();
        
        when(repository.findByUuid(uuid)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(new AssetTypeDTO(1L, uuid, "Updated", "Desc"));

        AssetTypeDTO result = service.update(uuid, dto);

        assertNotNull(result);
    }

    @Test
    void update_NotFound() {
        long uuid = 1L;
        AssetTypeCreateDTO dto = new AssetTypeCreateDTO("Updated", "Desc");
        
        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(BokerfiException.class, () -> service.update(uuid, dto));
    }
}
