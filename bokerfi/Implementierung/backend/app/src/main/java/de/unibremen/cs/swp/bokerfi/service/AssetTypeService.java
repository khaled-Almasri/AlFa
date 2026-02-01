package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.dto.AssetTypeDTO;
import de.unibremen.cs.swp.bokerfi.mapper.AssetTypeMapper;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import de.unibremen.cs.swp.bokerfi.persistence.AssetTypeRepository;
import de.unibremen.cs.swp.bokerfi.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Service für allgemeine Asset-Typ-Operationen.
 * <p>
 * Bietet Lesezugriff auf Asset-Typen.
 * </p>
 */
@Service
public class AssetTypeService
        extends BaseService<AssetType, AssetTypeDTO, AssetTypeRepository> {

    /**
     * Erstellt einen neuen AssetTypeService.
     *
     * @param repository Das Asset-Typ-Repository
     * @param mapper     Der Asset-Typ-Mapper
     */
    public AssetTypeService(
            AssetTypeRepository repository,
            AssetTypeMapper mapper
    ) {
        super(repository, mapper);
    }
}
