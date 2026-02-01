package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.PersonDTO;
import de.unibremen.cs.swp.bokerfi.mapper.PersonMapper;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import de.unibremen.cs.swp.bokerfi.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Service-Layer zum Lesen von Personen.
 * <p>
 * Bietet Lesezugriff auf Personen (Benutzer).
 * </p>
 */
@Service
public class PersonService
        extends BaseService<Person, PersonDTO, PersonRepository> {

    /**
     * Erstellt einen neuen PersonService.
     *
     * @param personRepository Das Personen-Repository
     * @param mapper           Der Personen-Mapper
     */
    public PersonService(PersonRepository personRepository,
                         PersonMapper mapper) {
        super(personRepository, mapper);
    }
}
