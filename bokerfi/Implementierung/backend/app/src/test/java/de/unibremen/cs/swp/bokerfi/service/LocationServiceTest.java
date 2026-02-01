package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.LocationDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository repository;
    @Mock
    private LocationMapper mapper;

    @InjectMocks
    private LocationService service;

    @Test
    void findById_Success() {
        long id = 1L;
        Location location = new Location();
        LocationDTO dto = new LocationDTO(id, 100L, "Name");

        when(repository.findById(id)).thenReturn(Optional.of(location));
        when(mapper.toDto(location)).thenReturn(dto);

        Optional<LocationDTO> result = service.findById(id);

        assertNotNull(result);
        verify(repository).findById(id);
    }
}
