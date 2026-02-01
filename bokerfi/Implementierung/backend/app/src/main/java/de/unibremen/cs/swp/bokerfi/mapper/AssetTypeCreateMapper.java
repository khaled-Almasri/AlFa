package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.AssetTypeCreateDTO;
import de.unibremen.cs.swp.bokerfi.model.AssetType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper nur für CreateDTO -> Entity.
 */
@Mapper(componentModel = "spring")


public interface AssetTypeCreateMapper extends CreateMapper<AssetType, AssetTypeCreateDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "description", defaultValue = "")
    AssetType toEntity(AssetTypeCreateDTO dto);
}
