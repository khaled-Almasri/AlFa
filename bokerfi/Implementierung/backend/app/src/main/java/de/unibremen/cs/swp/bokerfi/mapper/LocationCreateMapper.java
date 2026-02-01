package de.unibremen.cs.swp.bokerfi.mapper;

import de.unibremen.cs.swp.bokerfi.dto.LocationCreateDTO;
import de.unibremen.cs.swp.bokerfi.model.Location;
import org.mapstruct.Mapper;

/**
 * Mapper für die Erstellung von Standorten.
 */
@Mapper(componentModel = "spring")
public interface LocationCreateMapper extends CreateMapper<Location, LocationCreateDTO> {
    @Override
    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "uuid", ignore = true)
    @org.mapstruct.Mapping(target = "version", ignore = true)
    Location toEntity(LocationCreateDTO dto);
}
