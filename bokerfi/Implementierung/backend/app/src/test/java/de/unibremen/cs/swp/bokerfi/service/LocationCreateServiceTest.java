package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.LocationCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.mapper.LocationCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.LocationMapper;
import de.unibremen.cs.swp.bokerfi.model.Location;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
// unused import removed
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationCreateServiceTest {

    @Mock
    private LocationRepository repository;
    @Mock
    private LocationCreateMapper createMapper;
    @Mock
    private LocationMapper responseMapper;

    @InjectMocks
    private LocationCreateService service;

    @Test
    void create_Success() {
        LocationCreateDTO dto = new LocationCreateDTO("Room 1");
        Location location = new Location();
        LocationDTO responseDto = new LocationDTO(1L, 100L, "Room 1");

        when(repository.findByName("Room 1")).thenReturn(Optional.empty());
        when(createMapper.toEntity(dto)).thenReturn(location);
        when(repository.save(location)).thenReturn(location);
        when(responseMapper.toDto(location)).thenReturn(responseDto);

        LocationDTO result = service.create(dto);

        assertNotNull(result);
        verify(repository).save(location);
    }

    @Test
    void create_DuplicateName() {
        LocationCreateDTO dto = new LocationCreateDTO("Room 1");

        when(repository.findByName("Room 1")).thenReturn(Optional.of(new Location()));

        assertThrows(BokerfiException.class, () -> service.create(dto));
    }
}
