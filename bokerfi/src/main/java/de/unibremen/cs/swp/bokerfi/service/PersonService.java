package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-Schicht für das Lesen von Personen.
 */
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper mapper;

    /**
     * Konstruktor-basierte Dependency Injection.
     *
     * @param personRepository Repository für Personen
     * @param mapper Mapper für Person → PersonDTO
     */
    public PersonService(PersonRepository personRepository,
                         PersonMapper mapper) {
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    /**
     * Gibt alle Personen als DTOs zurück.
     *
     * @return Liste von PersonDTOs
     */
    public List<PersonDTO> findAll() {
        return mapper.toDtoList(personRepository.findAll());
    }
}
