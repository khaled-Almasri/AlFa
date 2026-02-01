package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.AssetTypeDTO;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import org.mapstruct.Mapper;



/**
 * Mapper für die Konvertierung von Asset-Typen.
 */
@Mapper(componentModel = "spring")
public interface AssetTypeMapper extends DTOMapper<AssetType, AssetTypeDTO> {
    @Override
    AssetTypeDTO toDto(AssetType entity);
}
