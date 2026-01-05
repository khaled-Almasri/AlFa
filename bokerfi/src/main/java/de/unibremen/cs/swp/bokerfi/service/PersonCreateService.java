package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.PersonCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.mapper.PersonCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.springframework.stereotype.Service;

/**
 * Service zum Erstellen von Personen.
 */
@Service
public class PersonCreateService {

    private final PersonRepository repository;
    private final PersonCreateMapper createMapper;
    private final PersonMapper personMapper;

    public PersonCreateService(PersonRepository repository,
                               PersonCreateMapper createMapper,
                               PersonMapper personMapper) {
        this.repository = repository;
        this.createMapper = createMapper;
        this.personMapper = personMapper;
    }

    /**
     * Erstellt eine neue Person und gibt sie als DTO zurück.
     */
    public PersonDTO createPerson(PersonCreateDTO dto) {
        Person person = createMapper.toEntity(dto);
        Person saved = repository.save(person);
        return personMapper.toDto(saved);
    }
}
