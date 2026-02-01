package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonUpdateDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
// unused import removed
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.mapper.PersonUpdateMapper;
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
class PersonUpdateServiceTest {

    @Mock
    private PersonRepository repository;
    @Mock
    private PersonUpdateMapper updateMapper;
    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonUpdateService service;

    @Test
    void applyUpdate_Success() {
        long uuid = 1L;
        long version = 0L;
        PersonUpdateDTO dto = new PersonUpdateDTO("New", "Name", "new@test.de", "123", Role.EMPLOYEE);
        Person person = new Person();
        person.setId(1L);
        person.setVersion(version);
        person.setRole(Role.EMPLOYEE);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(person));
        when(repository.saveAndFlush(person)).thenReturn(person);
        when(personMapper.toDto(person)).thenReturn(new PersonDTO(null, null, null, null, null, false, null, null, null));

        PersonDTO result = service.applyUpdate(dto, uuid, version);

        assertNotNull(result);
        verify(updateMapper).applyUpdate(any(), eq(person));
    }

    @Test
    void applyUpdate_VersionConflict() {
        long uuid = 1L;
        Person person = new Person();
        person.setVersion(1L);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(person));

        assertThrows(BokerfiException.class, () -> service.applyUpdate(null, uuid, 0L));
    }

    @Test
    void updateStatus_Success() {
        long uuid = 1L;
        Person person = new Person();
        person.setRole(Role.EMPLOYEE);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(person));

        service.updateStatus(uuid, false);

        assertFalse(person.isActive());
        verify(repository).save(person);
    }

    @Test
    void updateStatus_FailLastAdmin() {
        long uuid = 1L;
        Person person = new Person();
        person.setRole(Role.ADMIN);
        person.setActive(true);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(person));
        when(repository.countByRoleAndActiveTrue(Role.ADMIN)).thenReturn(1L);

        assertThrows(BokerfiException.class, () -> service.updateStatus(uuid, false));
    }
}
