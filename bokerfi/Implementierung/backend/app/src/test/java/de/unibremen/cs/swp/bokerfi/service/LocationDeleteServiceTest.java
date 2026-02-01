package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationDeleteServiceTest {

    @Mock
    private LocationRepository repository;

    @InjectMocks
    private LocationDeleteService service;

    @Test
    void deleteById_Success() {
        long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        service.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void deleteById_NotFound() {
        long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(BokerfiException.class, () -> service.deleteById(id));
    }
}
