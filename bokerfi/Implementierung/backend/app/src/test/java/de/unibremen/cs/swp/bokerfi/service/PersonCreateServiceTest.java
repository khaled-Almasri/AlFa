package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.mapper.PersonCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.model.Role;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
// unused import removed
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class PersonCreateServiceTest {

    @Mock
    private PersonRepository repository;
    @Mock
    private PersonCreateMapper createMapper;
    @Mock
    private PersonMapper responseMapper;

    @InjectMocks
    private PersonCreateService service;

    @Test
    void create_Success() {
        PersonCreateDTO createDTO = new PersonCreateDTO("test@test.de", "First", "Last", "123", "pass", Role.EMPLOYEE, java.util.Collections.emptyList());
        Person person = new Person();
        PersonDTO expectedDTO = new PersonDTO("test@test.de", "First", "Last", "123", Role.EMPLOYEE, true, 1L, 0L, java.util.Collections.emptyList());

        when(createMapper.toEntity(any())).thenReturn(person);
        when(repository.save(any())).thenReturn(person);
        when(responseMapper.toDto(any())).thenReturn(expectedDTO);

        PersonDTO result = service.create(createDTO);

        assertNotNull(result);
        verify(repository).save(person);
    }

    @Test
    void create_DuplicateEmail_ThrowsException() {
        PersonCreateDTO createDTO = new PersonCreateDTO("exists@test.de", "First", "Last", "123", "pass", Role.EMPLOYEE, java.util.Collections.emptyList());
        // Person person = new Person(); // unused 

        when(repository.findByEmailIgnoreCase("exists@test.de")).thenReturn(Optional.of(new Person()));
        
        assertThrows(IllegalArgumentException.class, () -> service.create(createDTO));
    }
}
