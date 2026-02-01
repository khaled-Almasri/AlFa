package de.unibremen.cs.swp.bokerfi.service.base;

import de.unibremen.cs.swp.bokerfi.mapper.CreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.DTOMapper;
import de.unibremen.cs.swp.bokerfi.model.DBEntity;
import de.unibremen.cs.swp.bokerfi.persistence.BaseRepository;
import jakarta.transaction.Transactional;

/**
 * Abstract base service for creating entities.
 *
 * @param <E>    The entity type
 * @param <CDTO> The create DTO type
 * @param <RDTO> The response DTO type
 * @param <REPO> The repository type
 */
public abstract class BaseCreateService<
        E extends DBEntity,
        C,
        R,
        P extends BaseRepository<E>> {

    protected final P repository;
    protected final CreateMapper<E, C> createMapper;
    protected final DTOMapper<E, R> responseMapper;

    /**
     * Constructs a new BaseCreateService.
     *
     * @param repository     The repository
     * @param createMapper   The mapper for creating entities
     * @param responseMapper The mapper for response DTOs
     */
    protected BaseCreateService(
            P repository,
            CreateMapper<E, C> createMapper,
            DTOMapper<E, R> responseMapper
    ) {
        this.repository = repository;
        this.createMapper = createMapper;
        this.responseMapper = responseMapper;
    }

    /**
     * Creates a new entity from the given DTO.
     *
     * @param createDTO The DTO containing the data for the new entity
     * @return The DTO of the created entity
     */
    @Transactional
    @SuppressWarnings("null")
    public R create(C createDTO) {
        E entity = createMapper.toEntity(createDTO);
        entity = beforeSave(entity);
        validate(entity, createDTO);
        E saved = repository.save(entity);
        saved = afterSave(saved);
        return responseMapper.toDto(saved);
    }

    /**
     * Hook method called before saving the entity.
     *
     * @param entity The entity to be saved
     * @return The entity to be saved (potentially modified)
     */
    protected E beforeSave(E entity) {
        return entity;
    }

    /**
     * Hook method called after saving the entity.
     *
     * @param entity The saved entity
     * @return The saved entity (potentially modified)
     */
    protected E afterSave(E entity) {
        return entity;
    }

    /**
     * Validates the entity before saving.
     *
     * @param entity    The entity to validate
     * @param createDTO The original create DTO
     */
    protected void validate(E entity, C createDTO) {
        // optional
    }
}
