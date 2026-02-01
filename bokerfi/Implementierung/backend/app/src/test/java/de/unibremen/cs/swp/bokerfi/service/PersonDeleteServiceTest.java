package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonDeleteServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonDeleteService service;

    @Test
    void deleteById_ShouldDeletePerson_WhenPersonExists() {
        Long uuid = 123L;
        Person mockPerson = new Person();
        // Assuming Person has a setter for UUID or we just mock the return
        // The service uses repository.findByUuid(uuid) then repository.deleteByUuid(uuid)
        
        when(repository.findByUuid(uuid)).thenReturn(Optional.of(mockPerson));

        service.deleteById(uuid);

        verify(repository).findByUuid(uuid);
        verify(repository).deleteByUuid(uuid);
    }

    @Test
    void deleteById_ShouldThrowException_WhenPersonDoesNotExist() {
        Long uuid = 456L;

        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

        BokerfiException ex = assertThrows(BokerfiException.class, () -> service.deleteById(uuid));
        assertEquals(ErrorCode.ENTITY_DOES_NOT_EXIST, ex.getErrorCode());
        
        // Ensure delete was NOT called
        verify(repository, never()).deleteByUuid(anyLong());
    }
}
