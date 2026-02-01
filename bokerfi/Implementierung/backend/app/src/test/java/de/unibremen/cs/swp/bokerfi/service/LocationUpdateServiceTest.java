package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.LocationCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationUpdateServiceTest {

    @Mock
    private LocationRepository repository;
    @Mock
    private LocationMapper mapper;

    @InjectMocks
    private LocationUpdateService service;

    @Test
    void update_Success() {
        long id = 1L;
        LocationCreateDTO dto = new LocationCreateDTO("Updated Room");
        Location location = new Location();
        
        when(repository.findById(id)).thenReturn(Optional.of(location));
        when(repository.save(location)).thenReturn(location);
        when(mapper.toDto(location)).thenReturn(new LocationDTO(id, 100L, "Name"));

        LocationDTO result = service.update(id, dto);

        assertNotNull(result);
        verify(repository).save(location);
    }

    @Test
    void update_NotFound() {
        long id = 1L;
        LocationCreateDTO dto = new LocationCreateDTO("Updated Room");

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BokerfiException.class, () -> service.update(id, dto));
    }
}
