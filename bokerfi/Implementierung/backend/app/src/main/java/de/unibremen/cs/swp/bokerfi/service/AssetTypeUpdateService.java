package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetTypeCreateDTO;
import de.unibremen.cs.swp.bokerfi.dto.AssetTypeDTO;
import de.unibremen.cs.swp.bokerfi.exception.BokerfiException;
import de.unibremen.cs.swp.bokerfi.exception.ErrorCode;
import de.unibremen.cs.swp.bokerfi.mapper.AssetTypeMapper;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service zum Aktualisieren bestehender Asset-Typen.
 * <p>
 * Handhabt Aktualisierungen von Asset-Typ-Eigenschaften wie dem Namen.
 * </p>
 */
@Service
@Transactional
public class AssetTypeUpdateService {

    private final AssetTypeRepository repository;
    private final AssetTypeMapper mapper;

    /**
     * Erstellt einen neuen AssetTypeUpdateService.
     *
     * @param repository Das Asset-Typ-Repository
     * @param mapper     Der Asset-Typ-Mapper
     */
    public AssetTypeUpdateService(AssetTypeRepository repository, AssetTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Aktualisiert einen Asset-Typ.
     *
     * @param uuid Die UUID des Asset-Typs
     * @param dto  Das DTO mit den aktualisierten Daten
     * @return Das aktualisierte AssetTypeDTO
     */
    @SuppressWarnings("null")
    public AssetTypeDTO update(long uuid, AssetTypeCreateDTO dto) {
        AssetType type = repository.findByUuid(uuid)
                .orElseThrow(() -> new BokerfiException(ErrorCode.ENTITY_DOES_NOT_EXIST, "AssetType not found"));

        if (dto.name() != null && !dto.name().isEmpty()) {
            type.setName(dto.name());
        }
        


        type = repository.save(type);
        return mapper.toDto(type);
    }
}
