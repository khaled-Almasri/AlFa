package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetTypeDTO;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetTypeServiceTest {

    @Mock
    private AssetTypeRepository repository;
    @Mock
    private AssetTypeMapper mapper;

    @InjectMocks
    private AssetTypeService service;

    @Test
    void findByUuid_Success() {
        long uuid = 1L;
        AssetType entity = new AssetType();
        AssetTypeDTO dto = new AssetTypeDTO(1L, uuid, "Name", "Desc");

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<AssetTypeDTO> result = service.findByUuid(uuid);

        assertNotNull(result);
    }
}
