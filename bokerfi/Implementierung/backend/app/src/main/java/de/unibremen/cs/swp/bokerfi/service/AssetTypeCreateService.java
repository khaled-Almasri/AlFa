package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetTypeCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.AssetTypeDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.mapper.AssetTypeCreateMapper;
import de.unibremen.cs.swp.bokerfi.mapper.AssetTypeMapper;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import de.unibremen.cs.swp.bokerfi.service.base.BaseCreateService;
import org.springframework.stereotype.Service;

/**
 * Service zum Erstellen neuer Asset-Typen.
 * <p>
 * Handhabt die Validierung und Erstellung von Asset-Typen.
 * </p>
 */
@Service
public class AssetTypeCreateService
        extends BaseCreateService<
        AssetType,
        AssetTypeCreateDTO,
        AssetTypeDTO,
        AssetTypeRepository> {

    /**
     * Erstellt einen neuen AssetTypeCreateService.
     *
     * @param repository     Das Asset-Typ-Repository
     * @param createMapper   Der Create-Mapper
     * @param responseMapper Der Response-Mapper
     */
    public AssetTypeCreateService(
            AssetTypeRepository repository,
            AssetTypeCreateMapper createMapper,
            AssetTypeMapper responseMapper
    ) {
        super(repository, createMapper, responseMapper);
    }

    @Override
    protected void validate(AssetType entity, AssetTypeCreateDTO dto) {
        if (repository.existsByName(dto.name())) {
            throw new BokerfiException(
                    ErrorCode.ENTITY_ALREADY_EXISTS,
                    "AssetType already exists"
            );
        }
    }
}
