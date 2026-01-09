package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static de.unibremen.cs.swp.bokerfi.exception.ErrorCode.ENTITY_DOES_NOT_EXIST;

/**
 * Service zum Löschen von Personen über die UUID.
 * Umsetzung gemäß Übungsblatt 11.
 */
@Service
public class PersonDeleteService {

    private final PersonRepository repository;

    public PersonDeleteService(final PersonRepository personRepository) {
        this.repository = personRepository;
    }

    /**
     * Löscht eine Person anhand ihrer UUID.
     * - Wenn die Person nicht existiert -> wirft BokerfiException (ENTITY_DOES_NOT_EXIST)
     * - Wenn sie existiert -> löscht sie
     */
    @Transactional
    public void deleteById(final Long uuid) {
        final Optional<Person> person = repository.findByUuid(uuid);

        if (person.isPresent()) {
            repository.deleteByUuid(uuid);
        } else {
            throw new BokerfiException(ENTITY_DOES_NOT_EXIST);
        }
    }
}
