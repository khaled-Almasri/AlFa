package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetTypeDeleteServiceTest {

    @Mock
    private AssetTypeRepository repository;

    @InjectMocks
    private AssetTypeDeleteService service;

    @Test
    void deleteById_Success() {
        long id = 1L;
        when(repository.existsById(id)).thenReturn(true); // Usage in Service
        
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
