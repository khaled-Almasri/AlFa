package de.unibremen.cs.swp.bokerfi.service.base;
import de.unibremen.cs.swp.bokerfi.mapper.DTOMapper;
import de.unibremen.cs.swp.bokerfi.model.DBEntity;
import de.unibremen.cs.swp.bokerfi.persistence.BaseRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract base class for service components.
 *
 * <p>
 * This class encapsulates basic read operations shared by multiple
 * concrete services. It operates generically on entities, DTOs, and repositories
 * to avoid code duplication and ensure a consistent service structure.
 * </p>
 *
 * @param <E> Type of the managed entity (must inherit from {@link DBEntity})
 * @param <D> Type of the associated Data Transfer Object
 * @param <R> Type of the repository used
 */
public abstract class BaseService<
        E extends DBEntity,
        D,
        R extends BaseRepository<E>
        > {

    protected final R repository;
    protected final DTOMapper<E, D> mapper;

    /**
     * Creates a new base service.
     *
     * @param repository Repository for accessing the persistence layer
     * @param mapper     Mapper for converting between entity and DTO
     */
    protected BaseService(R repository, DTOMapper<E, D> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    /**
     * returns a DTO for the given UUID if an entity exists.
     *
     * @param uuid technical UUID of the entity
     * @return an {@link Optional} containing the DTO or an empty Optional
     *         if no entity was found
     */
    public Optional<D> findByUuid(long uuid) {
        return repository.findByUuid(uuid).map(mapper::toDto);
    }
    /**
     * Returns all saved entities as a list of DTOs.
     *
     * @return List of all DTOs
     */
    public List<D> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    /**
     * Returns a DTO for the given technical ID if an entity exists.
     *
     * @param id technical ID of the entity
     * @return an {@link Optional} containing the DTO or an empty Optional
     *         if no entity was found
     */
    public Optional<D> findById(long id) {
        return repository.findById(id).map(mapper::toDto);
    }
}
