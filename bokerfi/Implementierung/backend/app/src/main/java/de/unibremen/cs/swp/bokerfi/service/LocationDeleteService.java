package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;

import de.unibremen.cs.swp.bokerfi.persistence.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import static de.unibremen.cs.swp.bokerfi.exception.ErrorCode.ENTITY_DOES_NOT_EXIST;

/**
 * Service zum Löschen von Standorten.
 * <p>
 * Löscht einen Standort anhand seiner UUID.
 * </p>
 */
@Service
public class LocationDeleteService {

    private final LocationRepository repository;

    /**
     * Erstellt einen neuen LocationDeleteService.
     *
     * @param repository Das Standort-Repository
     */
    public LocationDeleteService(LocationRepository repository) {
        this.repository = repository;
    }

    /**
     * Löscht einen Standort anhand seiner ID.
     *
     * @param id Die ID des Standorts
     */
    @Transactional
    @SuppressWarnings("null")
    public void deleteById(final Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new BokerfiException(ENTITY_DOES_NOT_EXIST);
        }
    }
}
