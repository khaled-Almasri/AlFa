package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetTypeCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.AssetTypeDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.mapper.AssetTypeCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.AssetTypeMapper;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
// unused import removed
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetTypeCreateServiceTest {

    @Mock
    private AssetTypeRepository repository;
    @Mock
    private AssetTypeCreateMapper createMapper;
    @Mock
    private AssetTypeMapper responseMapper;

    @InjectMocks
    private AssetTypeCreateService service;

    @Test
    void create_Success() {
        AssetTypeCreateDTO dto = new AssetTypeCreateDTO("Type1", "Desc");
        AssetType entity = new AssetType();
        AssetTypeDTO responseDto = new AssetTypeDTO(1L, 100L, "Type1", "Desc");

        when(repository.existsByName("Type1")).thenReturn(false);
        when(createMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(responseMapper.toDto(entity)).thenReturn(responseDto);

        AssetTypeDTO result = service.create(dto);

        assertNotNull(result);
        verify(repository).save(entity);
    }

    @Test
    void create_DuplicateName() {
        AssetTypeCreateDTO dto = new AssetTypeCreateDTO("Type1", "Desc");
        when(repository.existsByName("Type1")).thenReturn(true);

        assertThrows(BokerfiException.class, () -> service.create(dto));
    }
}
