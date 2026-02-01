package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository repository;
    @Mock
    private PersonMapper mapper;

    @InjectMocks
    private PersonService service;

    @Test
    void findByUuid_Success() {
        long uuid = 1L;
        Person person = new Person();
        PersonDTO dto = new PersonDTO("test", "first", "last", "123", null, true, uuid, 0L, null);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(person));
        when(mapper.toDto(person)).thenReturn(dto);


        
        Optional<PersonDTO> result = service.findByUuid(uuid);
        assertNotNull(result);
    }
}
