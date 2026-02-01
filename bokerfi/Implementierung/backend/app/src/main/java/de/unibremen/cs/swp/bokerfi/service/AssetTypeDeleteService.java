package de.unibremen.cs.swp.bokerfi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;

/**
 * Service zum Löschen von Asset-Typen.
 * <p>
 * Löscht einen Asset-Typ anhand seiner UUID.
 * </p>
 */
@Service
@Transactional
public class AssetTypeDeleteService {

    private final AssetTypeRepository repository;

    /**
     * Erstellt einen neuen AssetTypeDeleteService.
     *
     * @param repository Das Asset-Typ-Repository
     */
    public AssetTypeDeleteService(AssetTypeRepository repository) {
        this.repository = repository;
    }

    /**
     * Löscht einen Asset-Typ anhand seiner ID.
     *
     * @param id Die ID des Asset-Typs
     */
    public void deleteById(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "AssetType not found");
        }
    }
}
